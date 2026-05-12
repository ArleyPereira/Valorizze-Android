package app.valorizze.authentication.presenter.features.signup.validate.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.valorizze.authentication.presenter.features.signup.validate.action.ValidateSignupAction
import app.valorizze.authentication.presenter.features.signup.validate.event.ValidateSignupEvent
import app.valorizze.authentication.util.MainDispatcherTestRule
import app.valorizze.core.constants.Time.TIME_OTP_REQUEST
import app.valorizze.core.enums.confirmation.ConfirmationType
import app.valorizze.core.enums.feedback.FeedbackType
import app.valorizze.core.enums.input.recover.RecoverInputType.CODE
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.domain.dto.confirmation.ConfirmationDTO
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.model.feedback.Feedback
import app.valorizze.domain.usecase.remote.confirmation.ConfirmConfirmationUseCase
import app.valorizze.domain.usecase.remote.confirmation.ResendConfirmationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ValidateSignupViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherTestRule()

    private val confirmConfirmationUseCase: ConfirmConfirmationUseCase = mockk()
    private val resendConfirmationUseCase: ResendConfirmationUseCase = mockk()

    private lateinit var vm: ValidateSignupViewModel

    @Before
    fun setUp() {
        vm = ValidateSignupViewModel(
            confirmConfirmationUseCase = confirmConfirmationUseCase,
            resendConfirmationUseCase = resendConfirmationUseCase,
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "email" to "dev.arley.santana@gmail.com",
                    "password" to "123456",
                    "message" to "Código enviado",
                )
            )
        )
    }

    @Test
    fun `initial state should be correct`() {
        // GIVEN
        val email = "dev.arley.santana@gmail.com"
        val password = "123456"
        val message = "Código enviado"

        // WHEN

        // THEN
        val state = vm.state.value
        assertFalse(state.isLoading)
        assertTrue(state.code.isEmpty())
        assertEquals(email, state.email)
        assertEquals(password, state.password)
        assertEquals(message, state.message)
        assertTrue(state.resendTimer == TIME_OTP_REQUEST)
        assertTrue(state.feedback == null)
        assertTrue(state.inputError == null)
        assertTrue(state.sheetModel == null)
    }

    @Test
    fun `changing code should update state code`() {
        // GIVEN
        val code = "123456"

        // WHEN
        vm.dispatchAction(ValidateSignupAction.OnValueChange(code, CODE))

        // THEN
        val state = vm.state.value
        assertEquals(code, state.code)
        assertTrue(state.inputError == null)
    }

    @Test
    fun `empty code should set input error`() {
        runTest(mainDispatcherRule.dispatcher) {
            // GIVEN
            val code = ""

            vm.dispatchAction(ValidateSignupAction.OnValueChange(code, CODE))

            // WHEN
            vm.dispatchAction(ValidateSignupAction.ConfirmConfirmation)

            runCurrent()

            // THEN
            val state = vm.state.value
            assertEquals(CODE, state.inputError)
            coVerify(exactly = 0) { confirmConfirmationUseCase(any()) }
        }
    }

    @Test
    fun `dismiss feedback should remove feedback`() {
        // GIVEN
        val feedback = Feedback(
            title = "Conta confirmada com sucesso",
            type = FeedbackType.SUCCESS
        )

        assertEquals(null, vm.state.value.feedback)

        vm.dispatchAction(ValidateSignupAction.CreateFeedback(feedback))

        // WHEN
        vm.dispatchAction(ValidateSignupAction.DismissFeedback)

        // THEN
        val state = vm.state.value
        assertEquals(null, state.feedback)
    }

    @Test
    fun `clear bottom sheet should reset sheet model to null`() {
        runTest(mainDispatcherRule.dispatcher) {
            // GIVEN
            val code = "123456"
            val email = "dev.arley.santana@gmail.com"
            val feedbackMessage = "Erro ao criar conta"

            val response = BaseResponse<Unit>(
                resultStatus = ResultStatus.ERROR,
                message = feedbackMessage,
                status = 400
            )

            val dto = ConfirmationDTO(
                type = ConfirmationType.REGISTER,
                email = email,
                code = code
            )

            coEvery { confirmConfirmationUseCase(dto) } returns response

            vm.dispatchAction(ValidateSignupAction.OnValueChange(code, CODE))
            vm.dispatchAction(ValidateSignupAction.ConfirmConfirmation)

            runCurrent()

            assertEquals(feedbackMessage, vm.state.value.sheetModel?.message)

            // WHEN
            vm.dispatchAction(ValidateSignupAction.ClearBottomSheet)

            // THEN
            assertEquals(null, vm.state.value.sheetModel)
        }
    }

    @Test
    fun `successful confirm confirmation should emit login navigation`() =
        runTest(mainDispatcherRule.dispatcher) {
            // GIVEN
            val code = "123456"
            val email = "dev.arley.santana@gmail.com"
            val password = "123456"
            val feedbackMessage = "Conta criada com sucesso"

            val response = BaseResponse<Unit>(
                resultStatus = ResultStatus.SUCCESS,
                message = feedbackMessage,
                status = 200
            )

            val dto = ConfirmationDTO(
                type = ConfirmationType.REGISTER,
                email = email,
                code = code
            )

            coEvery { confirmConfirmationUseCase(dto) } returns response

            val navigateEvent = async {
                vm.event.first { it is ValidateSignupEvent.Navigation.Login }
            }

            vm.dispatchAction(ValidateSignupAction.OnValueChange(code, CODE))

            // WHEN
            vm.dispatchAction(ValidateSignupAction.ConfirmConfirmation)

            runCurrent()

            // THEN
            val event = navigateEvent.await() as ValidateSignupEvent.Navigation.Login
            assertEquals(email, event.email)
            assertEquals(password, event.password)
            assertEquals(feedbackMessage, event.message)

            coVerify(exactly = 1) { confirmConfirmationUseCase(dto) }
        }

    @Test
    fun `error confirm account and show bottom sheet and stop loading`() =
        runTest(mainDispatcherRule.dispatcher) {
            // GIVEN
            val code = "123456"
            val email = "dev.arley.santana@gmail.com"
            val feedbackMessage = "Erro ao criar conta"

            val response = BaseResponse<Unit>(
                resultStatus = ResultStatus.ERROR,
                message = feedbackMessage,
                status = 400
            )

            val dto = ConfirmationDTO(
                type = ConfirmationType.REGISTER,
                email = email,
                code = code
            )

            coEvery { confirmConfirmationUseCase(dto) } returns response

            vm.dispatchAction(ValidateSignupAction.OnValueChange(code, CODE))

            // WHEN
            vm.dispatchAction(ValidateSignupAction.ConfirmConfirmation)

            runCurrent()

            // THEN
            val state = vm.state.value
            assertTrue(state.sheetModel != null)
            assertEquals(feedbackMessage, state.sheetModel?.message)
            assertFalse(state.isLoading)

            coVerify(exactly = 1) { confirmConfirmationUseCase(dto) }
        }

    @Test
    fun `successful resend code and set feedback`() =
        runTest(mainDispatcherRule.dispatcher) {
            // GIVEN
            val email = "dev.arley.santana@gmail.com"
            val feedbackMessage = "Código reenviado com sucesso"

            val response = BaseResponse<Unit>(
                resultStatus = ResultStatus.SUCCESS,
                message = feedbackMessage,
                status = 200
            )

            val dto = ConfirmationDTO(
                type = ConfirmationType.PASSWORD_RESET,
                email = email
            )

            coEvery { resendConfirmationUseCase(dto) } returns response

            assertEquals(null, vm.state.value.feedback)

            // WHEN
            vm.dispatchAction(ValidateSignupAction.ResendCode)

            runCurrent()

            // THEN
            val state = vm.state.value
            assertFalse(state.isLoading)
            assertEquals(FeedbackType.SUCCESS, state.feedback?.type)
            assertEquals(feedbackMessage, state.feedback?.title)
            assertEquals(null, state.inputError)
            assertEquals(TIME_OTP_REQUEST, state.resendTimer)

            coVerify(exactly = 1) { resendConfirmationUseCase(dto) }
        }

    @Test
    fun `error resend code and show bottom sheet and stop loading`() =
        runTest(mainDispatcherRule.dispatcher) {
            // GIVEN
            val email = "dev.arley.santana@gmail.com"
            val feedbackMessage = "Erro ao reenviar confirmação"

            val response = BaseResponse<Unit>(
                resultStatus = ResultStatus.ERROR,
                message = feedbackMessage,
                status = 400
            )

            val dto = ConfirmationDTO(
                email = email,
                type = ConfirmationType.PASSWORD_RESET
            )

            coEvery { resendConfirmationUseCase(dto) } returns response

            // WHEN
            vm.dispatchAction(ValidateSignupAction.ResendCode)

            runCurrent()

            // THEN
            val state = vm.state.value
            assertTrue(state.sheetModel != null)
            assertEquals(feedbackMessage, state.sheetModel?.message)
            assertFalse(state.isLoading)

            coVerify(exactly = 1) { resendConfirmationUseCase(dto) }
        }

}