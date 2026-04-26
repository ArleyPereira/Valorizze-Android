package app.valorizze.authentication.presenter.features.recover.code.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.valorizze.authentication.presenter.features.recover.code.action.RecoverCodeAction
import app.valorizze.authentication.presenter.features.recover.code.event.RecoverCodeEvent
import app.valorizze.authentication.util.MainDispatcherRule
import app.valorizze.core.enums.input.recover.RecoverInputType.CODE
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.usecase.remote.confirmation.ValidateConfirmationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class RecoverCodeViewModelTest {

    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val validateConfirmationUseCase: ValidateConfirmationUseCase = mockk()

    private lateinit var vm: RecoverCodeViewModel

    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "email" to "dev.arley.santana@gmail.com",
                "message" to "Código enviado"
            )
        )
        vm = RecoverCodeViewModel(
            validateConfirmationUseCase = validateConfirmationUseCase,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `check values initial state`() {
        // GIVEN

        // WHEN

        // THEN
        val state = vm.state.value
        assertFalse(state.isLoading)
        assertEquals("", state.code)
        assertEquals("dev.arley.santana@gmail.com", state.email)
        assertEquals("Código enviado", state.message)
        assertEquals(null, state.inputError)
        assertEquals(null, state.sheetModel)
    }

    @Test
    fun `changing code updates state code`() {
        // GIVEN
        val code = "123456"

        // WHEN
        vm.dispatchAction(RecoverCodeAction.OnValueChange(code, CODE))

        // THEN
        assertEquals(code, vm.state.value.code)
        assertEquals(null, vm.state.value.inputError)
    }

    @Test
    fun `clicking validate with empty code sets inputError CODE`() = runTest {
        // GIVEN

        // WHEN
        vm.dispatchAction(RecoverCodeAction.ValidateConfirmation)

        advanceUntilIdle()

        // THEN
        val state = vm.state.value
        assertEquals(CODE, state.inputError)

        coVerify(exactly = 0) { validateConfirmationUseCase(any()) }
    }

    @Test
    fun `successful validate and stops loading and emits navigation`() = runTest {
        // GIVEN
        val code = "123456"

        coEvery { validateConfirmationUseCase(any()) } returns BaseResponse(
            resultStatus = ResultStatus.SUCCESS,
            status = 200
        )

        vm.dispatchAction(RecoverCodeAction.OnValueChange(value = code, type = CODE))

        val navigationEvent = async {
            vm.event.first { it is RecoverCodeEvent.Navigation.RecoverPasswordScreen }
        }

        // WHEN
        vm.dispatchAction(RecoverCodeAction.ValidateConfirmation)

        advanceUntilIdle()

        // THEN
        val event = navigationEvent.await() as RecoverCodeEvent.Navigation.RecoverPasswordScreen
        assertEquals("dev.arley.santana@gmail.com", event.email)
        assertEquals(code, event.code)

        coVerify(exactly = 1) { validateConfirmationUseCase(any()) }
    }

    @Test
    fun `error validate sets feedback and stops loading`() = runTest {
        // GIVEN
        val code = "123456"
        val feedbackMessage = "Código inválido"

        coEvery { validateConfirmationUseCase(any()) } returns BaseResponse(
            resultStatus = ResultStatus.ERROR,
            status = 400,
            message = feedbackMessage
        )

        vm.dispatchAction(RecoverCodeAction.OnValueChange(value = code, type = CODE))

        // WHEN
        vm.dispatchAction(RecoverCodeAction.ValidateConfirmation)

        // THEM
        val state = vm.state.first { it.sheetModel != null }

        coVerify(exactly = 1) { validateConfirmationUseCase(any()) }

        assertEquals(feedbackMessage, state.sheetModel?.message)
        assertFalse(state.isLoading)
    }

}