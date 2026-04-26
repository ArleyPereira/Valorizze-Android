package app.valorizze.authentication.presenter.features.recover.email.viewmodel

import app.valorizze.authentication.presenter.features.recover.email.action.RecoverEmailAction
import app.valorizze.authentication.presenter.features.recover.email.event.RecoverEmailEvent
import app.valorizze.authentication.util.MainDispatcherRule
import app.valorizze.core.enums.input.recover.RecoverInputType.EMAIL
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.usecase.remote.confirmation.CreateConfirmationUseCase
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

@OptIn(ExperimentalCoroutinesApi::class)
class RecoverEmailViewModelTest {

    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val createConfirmationUseCase: CreateConfirmationUseCase = mockk()

    private lateinit var vm: RecoverEmailViewModel

    @Before
    fun setUp() {
        vm = RecoverEmailViewModel(createConfirmationUseCase = createConfirmationUseCase)
    }

    @Test
    fun `check values initial state`() {
        // GIVEN
        val state = vm.state.value

        // WHEN

        // THEN
        assertFalse(state.isLoading)
        assertEquals("", state.email)
        assertEquals(null, state.inputError)
        assertEquals(null, state.sheetModel)
    }

    @Test
    fun `changing email updates state email`() {
        // GIVEN
        val email = "dev.arley.santana@gmail.com"

        // WHEN
        vm.dispatchAction(RecoverEmailAction.OnValueChange(value = email, type = EMAIL))

        // THEN
        assertEquals(email, vm.state.value.email)
        assertEquals(null, vm.state.value.inputError)
    }

    @Test
    fun `clicking crate with empty email sets inputError EMAIL`() = runTest {
        // GIVEN

        // WHEN
        vm.dispatchAction(RecoverEmailAction.CreateConfirmation)

        advanceUntilIdle()

        // THEN
        assertEquals(vm.state.value.inputError, EMAIL)
        coVerify(exactly = 0) { createConfirmationUseCase(any()) }
    }

    @Test
    fun `successful create confirmation and emits navigation end stops loading`() = runTest {
        // GIVEN
        val email = "dev.arley.santana@gmail.com"
        val feedbackMessage = "Confirmação criada com sucesso"

        coEvery { createConfirmationUseCase(any()) } returns BaseResponse(
            resultStatus = ResultStatus.SUCCESS,
            data = null,
            message = feedbackMessage,
            action = null,
            status = 200
        )

        vm.dispatchAction(RecoverEmailAction.OnValueChange(value = email, type = EMAIL))

        val navigationEvent = async {
            vm.event.first { it is RecoverEmailEvent.Navigation.RecoverCodeScreen }
        }

        // WHEN
        vm.dispatchAction(RecoverEmailAction.CreateConfirmation)

        advanceUntilIdle()

        // THEN
        val event = navigationEvent.await() as RecoverEmailEvent.Navigation.RecoverCodeScreen
        assertEquals(email, event.email)
        assertEquals(feedbackMessage, event.message)

        coVerify(exactly = 1) { createConfirmationUseCase(any()) }
        assertFalse(vm.state.value.isLoading)
    }

    @Test
    fun `error create confirmation sets feedback and stops loading`() = runTest {
        // GIVEN
        val email = "dev.arley.santana@gmail.com"
        val feedbackMessage = "Não foi possível criar a confirmação"

        coEvery { createConfirmationUseCase(any()) } returns BaseResponse(
            resultStatus = ResultStatus.ERROR,
            status = 400,
            action = null,
            message = feedbackMessage,
            data = null
        )

        vm.dispatchAction(RecoverEmailAction.OnValueChange(value = email, type = EMAIL))

        // WHEN
        vm.dispatchAction(RecoverEmailAction.CreateConfirmation)

        advanceUntilIdle()

        // THEN
        val state = vm.state.first { it.sheetModel != null }
        coVerify(exactly = 1) { createConfirmationUseCase(any()) }
        assertEquals(feedbackMessage, state.sheetModel?.message)
        assertFalse(state.isLoading)
    }

}