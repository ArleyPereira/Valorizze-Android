package app.valorizze.authentication.presenter.features.login.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.valorizze.authentication.R
import app.valorizze.authentication.di.authenticationModule
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.data.storage.preferences.LocalPreferences
import app.valorizze.domain.dto.auth.LoginDTO
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.model.user.User
import app.valorizze.domain.repository.remote.auth.AuthRepository
import app.valorizze.domain.usecase.remote.auth.LoginUseCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class LoginScreenSuccessInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var fakeAuthRepository: FakeAuthRepository

    @Before
    fun setUp() {
        fakeAuthRepository = FakeAuthRepository()

        startKoin {
            modules(
                authenticationModule,
                module {
                    single<AuthRepository> { fakeAuthRepository }
                    factory { LoginUseCase(get()) }
                    single { LocalPreferences() }
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun loginSuccess_fillsEmailAndPassword_clicksLogin_showsSuccessFeedback() {
        // GIVEN
        val emailLabel = composeRule.activity.getString(R.string.label_input_email_login_screen)
        val passwordLabel = composeRule.activity.getString(R.string.label_input_password_login_screen)
        val loginButtonText = composeRule.activity.getString(R.string.label_button_login_screen)

        val email = "john.doe@example.com"
        val password = "123456"
        val successMessage = "Login realizado com sucesso"

        composeRule.setContent {
            LoginScreen(
                navigateToSignupScreen = {},
                navigateToRecoverEmailScreen = {},
                navigateToValidateSignupScreen = { _, _, _ -> },
            )
        }

        // WHEN
        composeRule.onNode(hasText(emailLabel) and hasSetTextAction())
            .performTextInput(email)

        composeRule.onNode(hasText(passwordLabel) and hasSetTextAction())
            .performTextInput(password)

        composeRule.onNode(hasText(loginButtonText))
            .performClick()

        // THEN
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodes(hasText(successMessage)).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNode(hasText(successMessage)).assertIsDisplayed()
    }

    private class FakeAuthRepository : AuthRepository {
        override suspend fun login(loginDTO: LoginDTO): BaseResponse<User> {
            return BaseResponse(
                resultStatus = ResultStatus.SUCCESS,
                status = 200,
                message = "Login realizado com sucesso",
                action = null,
                data = User(
                    id = 1,
                    firstName = "John",
                    lastName = "Doe",
                    email = loginDTO.email,
                    token = "token",
                ),
            )
        }
    }
}

