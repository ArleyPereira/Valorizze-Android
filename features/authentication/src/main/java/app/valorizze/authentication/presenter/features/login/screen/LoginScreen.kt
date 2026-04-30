package app.valorizze.authentication.presenter.features.login.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.valorizze.authentication.R
import app.valorizze.authentication.presenter.features.login.action.LoginAction
import app.valorizze.authentication.presenter.features.login.state.LoginState
import app.valorizze.authentication.presenter.features.login.viewmodel.LoginViewModel
import app.valorizze.core.enums.illustration.IllustrationType
import app.valorizze.core.enums.input.login.LoginInputType
import app.valorizze.core.enums.sheet.BottomSheetType.NOT_CONFIRMED
import app.valorizze.design.presenter.components.bottom.screen.BottomScreenUI
import app.valorizze.design.presenter.components.bottom.sheet.content.generic.GenericSheetContent
import app.valorizze.design.presenter.components.bottom.sheet.default.DefaultBottomSheet
import app.valorizze.design.presenter.components.button.PrimaryButton
import app.valorizze.design.presenter.components.button.TextButtonUI
import app.valorizze.design.presenter.components.icon.illustration.DefaultIcon
import app.valorizze.design.presenter.components.snackbar.FeedbackUI
import app.valorizze.design.presenter.components.textfield.default.TextFieldUI
import app.valorizze.design.presenter.components.textfield.password.TextFieldPasswordUI
import app.valorizze.design.presenter.theme.ColorScheme
import app.valorizze.design.presenter.theme.HelloTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navigateToSignupScreen: () -> Unit,
    navigateToRecoverEmailScreen: () -> Unit,
    navigateToValidateSignupScreen: (email: String, password: String, message: String) -> Unit,
) {
    val viewModel = koinViewModel<LoginViewModel>()
    val state by viewModel.state.collectAsState()

    LoginContent(
        state = state,
        action = viewModel::dispatchAction,
        navigateToSignupScreen = navigateToSignupScreen,
        navigateToRecoverEmailScreen = navigateToRecoverEmailScreen,
        navigateToValidateSignupScreen = {
            navigateToValidateSignupScreen(
                state.email,
                state.password,
                state.message
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    state: LoginState,
    action: (LoginAction) -> Unit,
    navigateToSignupScreen: () -> Unit,
    navigateToRecoverEmailScreen: () -> Unit,
    navigateToValidateSignupScreen: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold(
        bottomBar = {
            BottomScreenUI(
                feedback = {
                    state.feedback?.let { feedback ->
                        FeedbackUI(
                            feedback = feedback,
                            onDismiss = { action(LoginAction.DismissFeedback) }
                        )
                    }
                },
                content = {}
            )
        },
        containerColor = ColorScheme.colorScheme.screen.backgroundPrimary,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                TextFieldUI(
                    modifier = Modifier,
                    value = state.email,
                    label = stringResource(R.string.label_input_email_login_screen),
                    isError = state.inputError == LoginInputType.EMAIL,
                    error = stringResource(R.string.message_email_invalid_format_login_screen),
                    leadingIcon = {
                        DefaultIcon(type = IllustrationType.IC_EMAIL_FILL)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = {
                        action(
                            LoginAction.OnValueChange(
                                value = it,
                                type = LoginInputType.EMAIL
                            )
                        )
                    }
                )

                TextFieldPasswordUI(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    value = state.password,
                    label = stringResource(R.string.label_input_password_login_screen),
                    isError = state.inputError == LoginInputType.PASSWORD,
                    error = stringResource(R.string.message_password_invalid_format_login_screen),
                    onValueChange = {
                        action(
                            LoginAction.OnValueChange(
                                value = it,
                                type = LoginInputType.PASSWORD
                            )
                        )
                    }
                )

                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.label_button_login_screen),
                    isLoading = state.isLoading,
                    onClick = {
                        focusManager.clearFocus()
                        action(LoginAction.OnSignIn)
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButtonUI(
                        text = stringResource(R.string.label_forgot_password_login_screen),
                        textColor = ColorScheme.colorScheme.defaultColor,
                        onClick = navigateToRecoverEmailScreen
                    )

                    TextButtonUI(
                        text = stringResource(R.string.label_sign_up_login_screen),
                        textColor = ColorScheme.colorScheme.defaultColor,
                        onClick = navigateToSignupScreen
                    )
                }
            }

            state.sheetModel?.let { sheetModel ->
                DefaultBottomSheet(
                    onDismissRequest = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            action(LoginAction.ClearBottomSheet)
                        }
                    },
                    sheetState = sheetState,
                    content = {
                        when (sheetModel.type) {
                            NOT_CONFIRMED -> {
                                GenericSheetContent(
                                    sheet = sheetModel,
                                    onFirstClick = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            action(LoginAction.ClearBottomSheet)
                                            navigateToValidateSignupScreen()
                                        }
                                    }
                                )
                            }

                            else -> {
                                GenericSheetContent(
                                    sheet = sheetModel,
                                    onFirstClick = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            action(LoginAction.ClearBottomSheet)
                                        }
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun LoginPreview() {
    HelloTheme {
        LoginContent(
            state = LoginState(),
            action = {} ,
            navigateToSignupScreen = {},
            navigateToRecoverEmailScreen = {},
            navigateToValidateSignupScreen = {}
        )
    }
}
