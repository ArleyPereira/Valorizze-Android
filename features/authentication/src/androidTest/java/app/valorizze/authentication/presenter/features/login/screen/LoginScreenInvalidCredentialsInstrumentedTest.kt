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
class LoginScreenInvalidCredentialsInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        startKoin {
            modules(
                authenticationModule,
                module {
                    single<AuthRepository> { FakeInvalidCredentialsAuthRepository() }
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
    fun invalidPassword_clickLogin_showsBottomSheet() {
        // GIVEN
        val emailLabel = composeRule.activity.getString(R.string.label_input_email_login_screen)
        val passwordLabel = composeRule.activity.getString(R.string.label_input_password_login_screen)
        val loginButtonText = composeRule.activity.getString(R.string.label_button_login_screen)

        val bottomSheetTitle = composeRule.activity.getString(app.valorizze.design.R.string.text_tile_generic_sheet_content)
        val bottomSheetPrimaryButton = composeRule.activity.getString(app.valorizze.design.R.string.text_second_button_generic_sheet_content)

        val invalidMessage = "Email ou senha inválidos"

        composeRule.setContent {
            LoginScreen(
                navigateToSignupScreen = {},
                navigateToRecoverEmailScreen = {},
                navigateToValidateSignupScreen = { _, _, _ -> },
            )
        }

        // WHEN
        composeRule.onNode(hasText(emailLabel) and hasSetTextAction())
            .performTextInput("arley@gmail.com")

        composeRule.onNode(hasText(passwordLabel) and hasSetTextAction())
            .performTextInput("senha-errada")

        composeRule.onNode(hasText(loginButtonText))
            .performClick()

        // THEN
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodes(hasText(invalidMessage)).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNode(hasText(bottomSheetTitle)).assertIsDisplayed()
        composeRule.onNode(hasText(invalidMessage)).assertIsDisplayed()
        composeRule.onNode(hasText(bottomSheetPrimaryButton)).assertIsDisplayed()
    }

    private class FakeInvalidCredentialsAuthRepository : AuthRepository {
        override suspend fun login(loginDTO: LoginDTO): BaseResponse<User> {
            return BaseResponse(
                resultStatus = ResultStatus.ERROR,
                status = 400,
                message = "Email ou senha inválidos",
                action = "invalid_data",
                data = null,
            )
        }
    }
}

