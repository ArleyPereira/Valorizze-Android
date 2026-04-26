package app.valorizze.authentication.presenter.features.recover.code.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.valorizze.authentication.presenter.features.recover.code.action.RecoverCodeAction
import app.valorizze.authentication.util.MainDispatcherRule
import app.valorizze.core.enums.input.recover.RecoverInputType.CODE
import app.valorizze.domain.usecase.remote.confirmation.ValidateConfirmationUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class RecoverCodeViewModelTest {

    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val validateConfirmationUseCase: ValidateConfirmationUseCase = mockk()

    private lateinit var vm: RecoverCodeViewModel

    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle(
            mapOf("email" to "dev.arley.santana@gmail.com")
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
        assertEquals("", state.email)
        assertEquals("", state.message)
        assertEquals(null, state.inputError)
        assertEquals(null, state.sheetModel)
    }

    @Test
    fun `changing email updates state email`() {
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

}