package app.valorizze.authentication.presenter.features.recover.password.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.valorizze.authentication.presenter.features.recover.password.action.RecoverPasswordAction
import app.valorizze.authentication.presenter.features.recover.password.event.RecoverPasswordEvent
import app.valorizze.authentication.util.MainDispatcherTestRule
import app.valorizze.core.enums.input.recover.RecoverInputType.PASSWORD
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.usecase.remote.confirmation.ConfirmConfirmationUseCase
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
class RecoverPasswordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherTestRule()

    private val confirmConfirmationUseCase: ConfirmConfirmationUseCase = mockk()

    private lateinit var vm: RecoverPasswordViewModel

    @Before
    fun setUp() {
        vm = RecoverPasswordViewModel(
            confirmConfirmationUseCase = confirmConfirmationUseCase,
            savedStateHandle = SavedStateHandle(
                mapOf("email" to "arley.dev.santana@gmail.com", "code" to "123456")
            )
        )
    }

    @Test
    fun `check values initial state`() {
        // GIVEN

        // WHEN

        // THEN
        val state = vm.state.value
        assertFalse(state.isLoading)
        assertEquals("arley.dev.santana@gmail.com", state.email)
        assertEquals("123456", state.code)
        assertEquals("", state.password)
        assertEquals(null, state.inputError)
        assertEquals(null, state.sheetModel)
    }

    @Test
    fun `changing password updates state password`() {
        // GIVEN
        val password = "teste123"

        // WHEN
        vm.dispatchAction(RecoverPasswordAction.OnValueChange(value = password, type = PASSWORD))

        // THEN
        val state = vm.state.value
        assertEquals(password, state.password)
        assertEquals(null, state.inputError)
    }

    @Test
    fun `clicking confirm with empty password sets inputError PASSWORD`() =
        runTest(mainDispatcherRule.dispatcher) {
            // GIVEN

            // WHEN
            vm.dispatchAction(RecoverPasswordAction.ConfirmConfirmation)

            advanceUntilIdle()

            // THEN
            assertEquals(PASSWORD, vm.state.value.inputError)

            coVerify(exactly = 0) { confirmConfirmationUseCase(any()) }
        }

    @Test
    fun `successful confirmation and stops loading and emits navigation`() =
        runTest(mainDispatcherRule.dispatcher) {
            // GIVEN
            val password = "teste123"
            val feedbackMessage = "Confirmação realizada com sucesso"

            coEvery { confirmConfirmationUseCase(any()) } returns BaseResponse(
                resultStatus = ResultStatus.SUCCESS,
                status = 200,
                message = feedbackMessage
            )

            vm.dispatchAction(
                RecoverPasswordAction.OnValueChange(
                    value = password,
                    type = PASSWORD
                )
            )

            val navigationEvent = async {
                vm.event.first { it is RecoverPasswordEvent.Navigation.LoginScreen }
            }

            // WHEN
            vm.dispatchAction(RecoverPasswordAction.ConfirmConfirmation)

            advanceUntilIdle()

            // THEN
            val event = navigationEvent.await() as RecoverPasswordEvent.Navigation.LoginScreen
            assertEquals(feedbackMessage, event.message)

            coVerify(exactly = 1) { confirmConfirmationUseCase(any()) }
        }

    @Test
    fun `error confirmation sets feedback and stops loading`() =
        runTest(mainDispatcherRule.dispatcher) {
            // GIVEN
            val password = "teste123"
            val feedbackMessage = "Não foi possível realizar a confirmação"

            coEvery { confirmConfirmationUseCase(any()) } returns BaseResponse(
                resultStatus = ResultStatus.ERROR,
                status = 400,
                message = feedbackMessage
            )

            vm.dispatchAction(
                RecoverPasswordAction.OnValueChange(
                    value = password,
                    type = PASSWORD
                )
            )

            // WHEN
            vm.dispatchAction(RecoverPasswordAction.ConfirmConfirmation)

            advanceUntilIdle()

            // THEN
            val state = vm.state.value

            coVerify(exactly = 1) { confirmConfirmationUseCase(any()) }

            assertEquals(feedbackMessage, state.sheetModel?.message)
            assertFalse(state.isLoading)
        }

}