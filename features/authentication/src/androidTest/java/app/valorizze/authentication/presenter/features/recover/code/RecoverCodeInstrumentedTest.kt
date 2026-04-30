package app.valorizze.authentication.presenter.features.recover.code

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
import app.valorizze.authentication.presenter.features.recover.code.screen.RecoverCodeScreen
import app.valorizze.authentication.presenter.features.recover.password.screen.RecoverPasswordContent
import app.valorizze.authentication.presenter.features.recover.password.state.RecoverPasswordState
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.repository.remote.confirmation.ConfirmationRepository
import app.valorizze.domain.usecase.remote.confirmation.ValidateConfirmationUseCase
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
class RecoverCodeInstrumentedTest {

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
                    factory { ValidateConfirmationUseCase(get()) }
                    single {
                        SavedStateHandle(
                            mapOf(
                                "email" to "dev.arley.santana@gmail.com",
                                "message" to "Se o e-mail existir em nossa base, enviamos um código. Verifique seu e-mail.",
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
    fun success_validateConfirmation_clickButton_navigateAndShowsPasswordInput() {
        // GIVEN
        val codeLabel = composeRule.activity.getString(R.string.label_code_recover_code_screen)
        val buttonLabel = composeRule.activity.getString(R.string.label_button_recover_flow)
        val passwordLabel =
            composeRule.activity.getString(R.string.label_code_recover_password_screen)

        val code = "123456"

        fakeConfirmationRepository.validateResponse = {
            BaseResponse(
                resultStatus = ResultStatus.SUCCESS,
                status = 200
            )
        }

        composeRule.setContent {
            var email by remember { mutableStateOf("") }
            var code by remember { mutableStateOf("") }
            var showRecoverPassword by remember { mutableStateOf(false) }

            if (showRecoverPassword) {
                RecoverPasswordContent(
                    state = RecoverPasswordState(
                        email = email,
                        code = code,
                    ),
                    action = {},
                    onBackPressed = {},
                )
            } else {
                RecoverCodeScreen(
                    navigateToRecoverPasswordScreen = { navEmail, navCode ->
                        email = navEmail
                        code = navCode
                        showRecoverPassword = true
                    },
                    onBackPressed = {},
                )
            }
        }

        // WHEN
        composeRule.onNode(hasText(codeLabel) and hasSetTextAction())
            .performTextInput(code)

        composeRule.onNode(hasText(buttonLabel))
            .performClick()

        // THEN
        composeRule.waitUntil(3_000) {
            composeRule.onAllNodes(hasText(passwordLabel)).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNode(hasText(passwordLabel)).assertIsDisplayed()
    }

    @Test
    fun invalid_validateConfirmation_clickButton_showsBottomSheet() {
        // GIVEN
        val codeLabel = composeRule.activity.getString(R.string.label_code_recover_code_screen)
        val buttonLabel = composeRule.activity.getString(R.string.label_button_recover_flow)

        val bottomSheetTitle =
            composeRule.activity.getString(RDesign.string.text_tile_generic_sheet_content)
        val bottomSheetPrimaryButton =
            composeRule.activity.getString(RDesign.string.text_second_button_generic_sheet_content)

        val code = "123456"
        val invalidMessage = "O código informado está incorreto. Verifique seu e-mail."

        fakeConfirmationRepository.validateResponse = {
            BaseResponse(
                resultStatus = ResultStatus.ERROR,
                status = 400,
                message = invalidMessage,
            )
        }

        composeRule.setContent {
            RecoverCodeScreen(
                navigateToRecoverPasswordScreen = { _, _ -> },
                onBackPressed = {},
            )
        }

        // WHEN
        composeRule.onNode(hasText(codeLabel) and hasSetTextAction())
            .performTextInput(code)

        composeRule.onNode(hasText(buttonLabel))
            .performClick()

        composeRule.waitUntil(3_000) {
            composeRule.onAllNodes(hasText(invalidMessage)).fetchSemanticsNodes().isNotEmpty()
        }

        // THEN
        composeRule.onNode(hasText(bottomSheetTitle)).assertIsDisplayed()
        composeRule.onNode(hasText(invalidMessage)).assertIsDisplayed()
        composeRule.onNode(hasText(bottomSheetPrimaryButton)).assertIsDisplayed()
    }

}