package app.valorizze.authentication.presenter.features.recover.password

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.valorizze.authentication.R
import app.valorizze.authentication.di.authenticationModule
import app.valorizze.authentication.fakes.respository.confirmation.FakeConfirmationRepository
import app.valorizze.authentication.presenter.features.login.screen.LoginContent
import app.valorizze.authentication.presenter.features.login.state.LoginState
import app.valorizze.authentication.presenter.features.recover.password.screen.RecoverPasswordScreen
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.repository.remote.confirmation.ConfirmationRepository
import app.valorizze.domain.usecase.remote.confirmation.ConfirmConfirmationUseCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import app.valorizze.design.R as RDesign

@RunWith(AndroidJUnit4::class)
class RecoverPasswordInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var fakeConfirmationRepository: FakeConfirmationRepository

    @Before
    fun setUp() {
        fakeConfirmationRepository = FakeConfirmationRepository()

        startKoin {
            modules(
                authenticationModule,
                module {
                    single<ConfirmationRepository> { fakeConfirmationRepository }
                    single { ConfirmConfirmationUseCase(get()) }
                    single {
                        SavedStateHandle(
                            mapOf(
                                "email" to "dev.arley.santana@gmail.com",
                                "code" to "123456"
                            )
                        )
                    }
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun success_recoverPassword_clickButton_navigateAndShowFeedback() {
        // GIVEN
        val passwordLabel =
            composeRule.activity.getString(R.string.label_code_recover_password_screen)
        val buttonLabel =
            composeRule.activity.getString(R.string.label_button_recover_password_screen)
        val buttonLoginLabel = composeRule.activity.getString(R.string.label_button_login_screen)

        val newPassword = "123456"

        fakeConfirmationRepository.confirmResponse = {
            BaseResponse(
                resultStatus = ResultStatus.SUCCESS,
                status = 200,
                message = "Senha alterada com sucesso"
            )
        }

        composeRule.setContent {
            var showLoginScreen by remember { mutableStateOf(false) }
            var message by remember { mutableStateOf("") }

            if (showLoginScreen) {
                LoginContent(
                    state = LoginState(message = message),
                    action = {},
                    navigateToSignupScreen = {},
                    navigateToRecoverEmailScreen = {},
                    navigateToValidateSignupScreen = {}
                )
            } else {
                RecoverPasswordScreen(
                    navigateToLoginScreen = { navMessage ->
                        message = navMessage
                        showLoginScreen = true
                    },
                    onBackPressed = {}
                )
            }
        }

        // WHEN
        composeRule.onNode(hasText(passwordLabel) and hasSetTextAction())
            .performTextInput(newPassword)

        composeRule.onNode(hasText(buttonLabel))
            .performClick()

        // THEN
        composeRule.waitUntil(3_000) {
            composeRule.onAllNodes(hasText(buttonLoginLabel)).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNode(hasText(buttonLoginLabel)).assertIsDisplayed()
    }

    @Test
    fun invalid_recoverPassword_clickButton_showBottomSheet() {
        // GIVEN
        val passwordLabel =
            composeRule.activity.getString(R.string.label_code_recover_password_screen)
        val buttonLabel =
            composeRule.activity.getString(R.string.label_button_recover_password_screen)

        val bottomSheetTitle =
            composeRule.activity.getString(RDesign.string.text_tile_generic_sheet_content)
        val bottomSheetPrimaryButton =
            composeRule.activity.getString(RDesign.string.text_second_button_generic_sheet_content)

        val newPassword = "123456"
        val message = "Não foi possível alterar a senha"

        fakeConfirmationRepository.confirmResponse = {
            BaseResponse(
                resultStatus = ResultStatus.ERROR,
                status = 400,
                message = message
            )
        }

        composeRule.setContent {
            RecoverPasswordScreen(
                navigateToLoginScreen = {},
                onBackPressed = {}
            )
        }

        // WHEN
        composeRule.onNode(hasText(passwordLabel) and hasSetTextAction())
            .performTextInput(newPassword)

        composeRule.onNode(hasText(buttonLabel))
            .performClick()

        // THEN
        composeRule.waitUntil(3_000) {
            composeRule.onAllNodes(hasText(message)).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNode(hasText(bottomSheetTitle)).assertIsDisplayed()
        composeRule.onNode(hasText(message)).assertIsDisplayed()
        composeRule.onNode(hasText(bottomSheetPrimaryButton)).assertIsDisplayed()
    }

}