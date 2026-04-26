package app.valorizze.authentication.presenter.features.login.viewmodel

import app.valorizze.authentication.presenter.features.login.action.LoginAction
import app.valorizze.authentication.presenter.features.login.event.LoginEvent
import app.valorizze.authentication.util.MainDispatcherRule
import app.valorizze.core.enums.input.login.LoginInputType.EMAIL
import app.valorizze.core.enums.input.login.LoginInputType.PASSWORD
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.data.storage.preferences.LocalPreferences
import app.valorizze.domain.dto.auth.LoginDTO
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.model.user.User
import app.valorizze.domain.usecase.remote.auth.LoginUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val loginUseCase: LoginUseCase = mockk()
    private val localPreferences: LocalPreferences = mockk(relaxed = true)

    private lateinit var vm: LoginViewModel

    @Before
    fun setUp() {
        vm = LoginViewModel(
            loginUseCase = loginUseCase,
            localPreferences = localPreferences,
        )
    }

    @Test
    fun `changing email updates state email`() = runTest {
        // GIVEN
        val email = "dev.arley.santana@gmail.com"

        // WHEN
        vm.dispatchAction(LoginAction.OnValueChange(value = email, type = EMAIL))

        // THEN
        assertEquals(email, vm.state.value.email)
        assertEquals(null, vm.state.value.inputError)
    }

    @Test
    fun `changing password updates state password`() = runTest {
        // GIVEN
        val password = "teste123"

        // WHEN
        vm.dispatchAction(LoginAction.OnValueChange(value = password, type = PASSWORD))

        // THEN
        assertEquals(password, vm.state.value.password)
        assertEquals(null, vm.state.value.inputError)
    }

    @Test
    fun `clicking login with empty email sets inputError EMAIL`() = runTest {
        // GIVEN

        // WHEN
        vm.dispatchAction(LoginAction.OnSignIn)
        advanceUntilIdle()

        // THEN
        val state = vm.state.first { it.inputError != null }
        assertEquals(EMAIL, state.inputError)
        coVerify(exactly = 0) { loginUseCase(any()) }
    }

    @Test
    fun `clicking login with empty password sets inputError PASSWORD`() = runTest {
        // GIVEN
        val email = "dev.arley.santana@gmail.com"

        // WHEN
        vm.dispatchAction(LoginAction.OnValueChange(value = email, type = EMAIL))
        vm.dispatchAction(LoginAction.OnSignIn)
        advanceUntilIdle()

        // THEN
        assertEquals(PASSWORD, vm.state.value.inputError)
        coVerify(exactly = 0) { loginUseCase(any()) }
    }

    @Test
    fun `successful login saves user emits navigation and stops loading`() = runTest {
        // GIVEN
        val user = User(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            token = "token",
        )

        coEvery { loginUseCase(any()) } returns BaseResponse(
            resultStatus = ResultStatus.SUCCESS,
            data = user,
            message = null,
            action = null,
            status = 200
        )

        vm.dispatchAction(LoginAction.OnValueChange("john.doe@example.com", EMAIL))
        vm.dispatchAction(LoginAction.OnValueChange("123456", PASSWORD))

        // IMPORTANT: o Channel é rendezvous; se ninguém estiver coletando,
        // o send() suspende e o ViewModel pode não chegar a setar isLoading = false.
        val navigationEvent = async {
            vm.event.first { it is LoginEvent.Navigation.Main }
        }

        // WHEN
        vm.dispatchAction(LoginAction.OnSignIn)

        advanceUntilIdle()

        // THEN
        coVerify(exactly = 1) {
            loginUseCase(LoginDTO(email = "john.doe@example.com", password = "123456",))
        }

        verify(exactly = 1) { localPreferences.saveUser(user) }

        navigationEvent.await()

        assertFalse(vm.state.value.isLoading)
    }

    @Test
    fun `error login sets feedback and bottom sheet and stops loading`() = runTest {
        // GIVEN
        coEvery { loginUseCase(any()) } returns BaseResponse(
            resultStatus = ResultStatus.ERROR,
            data = null,
            message = "Credenciais inválidas",
            action = "invalid_data",
            status = 400,
        )

        vm.dispatchAction(LoginAction.OnValueChange("john.doe@example.com", EMAIL))
        vm.dispatchAction(LoginAction.OnValueChange("wrong", PASSWORD))

        // WHEN
        vm.dispatchAction(LoginAction.OnSignIn)

        advanceUntilIdle()

        // THEN
        val state = vm.state.first { it.message == "Credenciais inválidas" }
        assertFalse(state.isLoading)
        assertEquals("Credenciais inválidas", state.message)
        assertTrue(state.feedback != null)
        assertTrue(state.sheetModel != null)

        verify(exactly = 0) { localPreferences.saveUser(any()) }
    }

}

