package app.valorizze.authentication.presenter.features.recover.email

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
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.valorizze.authentication.R
import app.valorizze.authentication.di.authenticationModule
import app.valorizze.authentication.fakes.respository.confirmation.FakeConfirmationRepository
import app.valorizze.authentication.presenter.features.recover.code.screen.RecoverCodeContent
import app.valorizze.authentication.presenter.features.recover.code.state.RecoverCodeState
import app.valorizze.authentication.presenter.features.recover.email.screen.RecoverEmailScreen
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.repository.remote.confirmation.ConfirmationRepository
import app.valorizze.domain.usecase.remote.confirmation.CreateConfirmationUseCase
import java.util.concurrent.atomic.AtomicBoolean
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class RecoverEmailInstrumentedTest {

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
                    factory { CreateConfirmationUseCase(get()) }
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun success_createConfirmation_clickButton_navigatesAndShowsCodeInput() {
        // GIVEN
        val emailLabel = composeRule.activity.getString(R.string.label_input_email_signup_screen)
        val buttonLabel = composeRule.activity.getString(R.string.label_button_recover_flow)
        val codeLabel = composeRule.activity.getString(R.string.label_code_recover_code_screen)

        val email = "dev.arley.santana@gmail.com"
        val message = "Se o e-mail existir em nossa base, enviamos um código. Verifique seu e-mail."
        val navigated = AtomicBoolean(false)

        fakeConfirmationRepository.createResponse = {
            BaseResponse(
                resultStatus = ResultStatus.SUCCESS,
                status = 200,
                message = message,
            )
        }

        composeRule.setContent {
            var nextEmail by remember { mutableStateOf("") }
            var nextMessage by remember { mutableStateOf("") }
            var showRecoverCode by remember { mutableStateOf(false) }

            if (showRecoverCode) {
                RecoverCodeContent(
                    state = RecoverCodeState(
                        email = nextEmail,
                        message = nextMessage,
                    ),
                    action = {},
                    onBackPressed = {},
                )
            } else {
                RecoverEmailScreen(
                    navigateToRecoverCodeScreen = { navEmail, navMessage ->
                        nextEmail = navEmail
                        nextMessage = navMessage
                        showRecoverCode = true
                        navigated.set(true)
                    },
                    onBackPressed = {},
                )
            }
        }

        // WHEN
        composeRule.onNode(hasText(emailLabel) and hasSetTextAction())
            .performTextInput(email)

        composeRule.onNode(hasText(buttonLabel))
            .performClick()

        // THEN
        composeRule.waitUntil(5_000) {
            navigated.get()
        }

        composeRule.onNode(hasText(codeLabel)).assertIsDisplayed()
    }

    @Test
    fun invalid_createConfirmation_clickButton_showsBottomSheet() {
        // GIVEN
        val emailLabel = composeRule.activity.getString(R.string.label_input_email_signup_screen)
        val buttonLabel = composeRule.activity.getString(R.string.label_button_recover_flow)

        val bottomSheetTitle =
            composeRule.activity.getString(app.valorizze.design.R.string.text_tile_generic_sheet_content)
        val bottomSheetPrimaryButton =
            composeRule.activity.getString(app.valorizze.design.R.string.text_second_button_generic_sheet_content)

        val email = "dev.arley.santana@gmail.com"
        val invalidMessage = "Não foi possível criar a confirmação"

        fakeConfirmationRepository.createResponse = {
            BaseResponse(
                resultStatus = ResultStatus.ERROR,
                status = 400,
                message = invalidMessage,
            )
        }

        composeRule.setContent {
            RecoverEmailScreen(
                navigateToRecoverCodeScreen = { _, _ -> },
                onBackPressed = {},
            )
        }

        // WHEN
        composeRule.onNode(hasText(emailLabel) and hasSetTextAction())
            .performTextInput(email)

        composeRule.onNode(hasText(buttonLabel))
            .performClick()

        // THEN
        composeRule.waitUntil(3_000) {
            composeRule.onAllNodes(hasText(invalidMessage)).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNode(hasText(bottomSheetTitle)).assertIsDisplayed()
        composeRule.onNode(hasText(invalidMessage)).assertIsDisplayed()
        composeRule.onNode(hasText(bottomSheetPrimaryButton)).assertIsDisplayed()
    }

}