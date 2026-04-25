package app.valorizze.authentication.presenter.features.login.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.valorizze.authentication.R
import app.valorizze.authentication.di.authenticationModule
import app.valorizze.data.di.dataModule
import app.valorizze.domain.di.domainModule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
class LoginScreenErrorMessageTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        startKoin {
            modules(
                domainModule,
                dataModule,
                authenticationModule,
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun clickingLoginWithEmptyEmail_showsEmailErrorMessage() {
        // GIVEN
        composeRule.setContent {
            LoginScreen(
                navigateToMainScreen = {},
                navigateToSignupScreen = {},
                navigateToRecoverEmailScreen = {},
                navigateToValidateSignupScreen = { _, _, _ -> },
            )
        }

        val loginButtonText = composeRule.activity.getString(R.string.label_button_login_screen)
        val emailErrorText = composeRule.activity.getString(R.string.message_email_invalid_format_login_screen)

        // WHEN
        composeRule.onNodeWithText(loginButtonText).performClick()

        // THEN
        composeRule.onNodeWithText(emailErrorText).assertIsDisplayed()
    }
}

