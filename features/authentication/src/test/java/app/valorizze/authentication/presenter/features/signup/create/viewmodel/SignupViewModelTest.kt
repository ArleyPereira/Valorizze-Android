package app.valorizze.authentication.presenter.features.signup.create.viewmodel

import app.valorizze.authentication.presenter.features.signup.create.action.SignupAction
import app.valorizze.authentication.presenter.features.signup.create.event.SignupEvent
import app.valorizze.authentication.util.MainDispatcherTestRule
import app.valorizze.core.enums.action.ActionType
import app.valorizze.core.enums.input.signup.SignupInputType.EMAIL
import app.valorizze.core.enums.input.signup.SignupInputType.FIRST_NAME
import app.valorizze.core.enums.input.signup.SignupInputType.LAST_NAME
import app.valorizze.core.enums.input.signup.SignupInputType.PASSWORD
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.model.user.User
import app.valorizze.domain.usecase.remote.user.CreateUserUseCase
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignupViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherTestRule()

    private val createUserUseCase: CreateUserUseCase = mockk()

    private lateinit var vm: SignupViewModel

    @Before
    fun setUp() {
        vm = SignupViewModel(createUserUseCase)
    }

    @Test
    fun `initial state should be correct`() {
        // GIVEN
        val state = vm.state.value

        // WHEN

        // THEN
        assertFalse(state.isLoading)
        assertTrue(state.firstName.isEmpty())
        assertTrue(state.lastName.isEmpty())
        assertTrue(state.email.isEmpty())
        assertTrue(state.password.isEmpty())
        assertFalse(state.onTermsChecked)
        assertTrue(state.sheetModel == null)
        assertTrue(state.inputError == null)
    }

    @Test
    fun `changing firstName should update state firstName`() {
        // GIVEN
        val firstName = "Arley"

        // WHEN
        vm.dispatchAction(SignupAction.OnValueChange(value = firstName, type = FIRST_NAME))

        // THEN
        assertEquals(firstName, vm.state.value.firstName)
        assertEquals(null, vm.state.value.inputError)
    }

    @Test
    fun `changing lastName should update state lastName`() {
        // GIVEN
        val lastName = "Santana"

        // WHEN
        vm.dispatchAction(SignupAction.OnValueChange(value = lastName, type = LAST_NAME))

        // THEN
        assertEquals(lastName, vm.state.value.lastName)
        assertEquals(null, vm.state.value.inputError)
    }

    @Test
    fun `changing email should update state email`() {
        // GIVEN
        val email = "dev.arley.santana@gmail.com"

        // WHEN
        vm.dispatchAction(SignupAction.OnValueChange(value = email, type = EMAIL))

        // THEN
        assertEquals(email, vm.state.value.email)
        assertEquals(null, vm.state.value.inputError)
    }

    @Test
    fun `changing password should update state password`() {
        // GIVEN
        val password = "123456"

        // WHEN
        vm.dispatchAction(SignupAction.OnValueChange(value = password, type = PASSWORD))

        // THEN
        assertEquals(password, vm.state.value.password)
        assertEquals(null, vm.state.value.inputError)
    }

    @Test
    fun `changing terms should update state terms`() {
        // GIVEN
        val terms = true

        // WHEN
        vm.dispatchAction(SignupAction.OnTermsChange)

        // THEN
        assertEquals(terms, vm.state.value.onTermsChecked)
    }

    @Test
    fun `successful create account and emit navigate event`() =
        runTest(mainDispatcherRule.dispatcher) {
            // GIVEN
            val firstName = "Arley"
            val lastName = "Santana"
            val email = "dev.arley.santana@gmail.com"
            val password = "123456"

            val userResponse = User(
                id = 1,
                firstName = firstName,
                lastName = lastName,
                email = email,
                createdAt = "2026-05-05T22:40:34.365Z",
                updatedAt = "2026-05-05T22:40:34.365Z"
            )

            val messageFeedback = "Quase lá! Confirme seu e-mail para finalizar o seu cadastro."

            coEvery { createUserUseCase(any()) } returns BaseResponse(
                resultStatus = ResultStatus.SUCCESS,
                data = userResponse,
                message = messageFeedback,
                action = ActionType.NOT_CONFIRMED.action,
                status = 200
            )

            val navigationEvent = async {
                vm.event.first() as SignupEvent.Navigation.Validate
            }

            vm.dispatchAction(SignupAction.OnValueChange(firstName, FIRST_NAME))
            vm.dispatchAction(SignupAction.OnValueChange(lastName, LAST_NAME))
            vm.dispatchAction(SignupAction.OnValueChange(email, EMAIL))
            vm.dispatchAction(SignupAction.OnValueChange(password, PASSWORD))
            vm.dispatchAction(SignupAction.OnTermsChange)

            // WHEN
            vm.dispatchAction(SignupAction.CreateUser)

            advanceUntilIdle()

            // THEN
            val event = navigationEvent.await()
            assertEquals(email, event.email)
            assertEquals(password, event.password)
            assertEquals(messageFeedback, event.message)

            coVerify(exactly = 1) { createUserUseCase(any()) }
            assertFalse(vm.state.value.isLoading)
        }

    @Test
    fun `error create account set feedback and show bottom sheet and stop loading`() =
        runTest(mainDispatcherRule.dispatcher) {
            // GIVEN
            val firstName = "Arley"
            val lastName = "Santana"
            val email = "dev.arley.santana@gmail.com"
            val password = "123456"

            val messageFeedback = "Por favor, tente novamente em alguns instantes."

            coEvery { createUserUseCase(any()) } returns BaseResponse(
                resultStatus = ResultStatus.ERROR,
                message = messageFeedback,
                status = 400
            )

            vm.dispatchAction(SignupAction.OnValueChange(firstName, FIRST_NAME))
            vm.dispatchAction(SignupAction.OnValueChange(lastName, LAST_NAME))
            vm.dispatchAction(SignupAction.OnValueChange(email, EMAIL))
            vm.dispatchAction(SignupAction.OnValueChange(password, PASSWORD))
            vm.dispatchAction(SignupAction.OnTermsChange)

            // WHEN
            vm.dispatchAction(SignupAction.CreateUser)

            advanceUntilIdle()

            // THEN
            val state = vm.state.value
            assertFalse(vm.state.value.isLoading)
            assertEquals(messageFeedback, state.sheetModel?.message)
            assertTrue(state.sheetModel != null)

            coVerify(exactly = 1) { createUserUseCase(any()) }
        }

}