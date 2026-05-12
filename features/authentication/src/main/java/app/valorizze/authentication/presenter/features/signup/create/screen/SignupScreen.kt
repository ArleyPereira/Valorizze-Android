package app.valorizze.authentication.presenter.features.signup.create.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.valorizze.authentication.R
import app.valorizze.authentication.presenter.features.signup.create.action.SignupAction
import app.valorizze.authentication.presenter.features.signup.create.event.SignupEvent
import app.valorizze.authentication.presenter.features.signup.create.state.SignupState
import app.valorizze.authentication.presenter.features.signup.create.viewmodel.SignupViewModel
import app.valorizze.core.enums.illustration.IllustrationType
import app.valorizze.core.enums.input.signup.SignupInputType
import app.valorizze.core.enums.sheet.BottomSheetType.NOT_CONFIRMED
import app.valorizze.core.functions.browser.openUrlInBrowser
import app.valorizze.design.presenter.components.bar.top.TopAppBarUI
import app.valorizze.design.presenter.components.bottom.screen.BottomScreenUI
import app.valorizze.design.presenter.components.bottom.sheet.content.generic.GenericSheetContent
import app.valorizze.design.presenter.components.bottom.sheet.default.DefaultBottomSheet
import app.valorizze.design.presenter.components.button.PrimaryButton
import app.valorizze.design.presenter.components.icon.illustration.getDrawableIllustration
import app.valorizze.design.presenter.components.snackbar.FeedbackUI
import app.valorizze.design.presenter.components.terms.TermsOfUseUI
import app.valorizze.design.presenter.components.textfield.default.TextFieldUI
import app.valorizze.design.presenter.components.textfield.password.TextFieldPasswordUI
import app.valorizze.design.presenter.theme.ColorScheme
import app.valorizze.design.presenter.theme.HelloTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignupScreen(
    navigateToValidateSignupScreen: (email: String, password: String, message: String) -> Unit,
    onBackPressed: () -> Unit
) {
    val viewModel = koinViewModel<SignupViewModel>()
    val state by viewModel.state.collectAsState()
    val event by viewModel.event.collectAsStateWithLifecycle(initialValue = SignupEvent.Idle)

    when (val currentState = event) {
        is SignupEvent.Navigation.Validate -> {
            navigateToValidateSignupScreen(
                currentState.email,
                currentState.password,
                currentState.message
            )
        }

        else -> {}
    }

    SignupContent(
        state = state,
        action = viewModel::dispatchAction,
        navigateToValidateSignupScreen = {
            navigateToValidateSignupScreen(
                state.email,
                state.password,
                state.message
            )
        },
        onBackPressed = onBackPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignupContent(
    state: SignupState,
    action: (SignupAction) -> Unit,
    navigateToValidateSignupScreen: () -> Unit,
    onBackPressed: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold(
        topBar = {
            TopAppBarUI(
                title = stringResource(R.string.label_title_signup_screen),
                onBackPressed = onBackPressed
            )
        },
        bottomBar = {
            BottomScreenUI(
                feedback = {
                    state.feedback?.let { feedback ->
                        FeedbackUI(
                            feedback = feedback,
                            onDismiss = { action(SignupAction.DismissFeedback) }
                        )
                    }
                },
                content = {
                    PrimaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 24.dp
                            ),
                        text = stringResource(R.string.label_button_signup_screen),
                        isLoading = state.isLoading,
                        onClick = {
                            focusManager.clearFocus()
                            action(SignupAction.CreateUser)
                        }
                    )
                }
            )
        },
        containerColor = ColorScheme.colorScheme.screen.backgroundPrimary,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextFieldUI(
                    value = state.firstName,
                    label = stringResource(R.string.label_input_first_name_signup_screen),
                    isError = state.inputError == SignupInputType.FIRST_NAME,
                    error = stringResource(R.string.message_first_name_invalid_format_signup_screen),
                    leadingIcon = {
                        Icon(
                            painter = getDrawableIllustration(IllustrationType.IC_PERSON_FILL),
                            contentDescription = null,
                            tint = ColorScheme.colorScheme.icon.default
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = {
                        action(
                            SignupAction.OnValueChange(
                                value = it,
                                type = SignupInputType.FIRST_NAME
                            )
                        )
                    }
                )

                TextFieldUI(
                    value = state.lastName,
                    label = stringResource(R.string.label_input_last_name_signup_screen),
                    isError = state.inputError == SignupInputType.LAST_NAME,
                    error = stringResource(R.string.message_last_name_invalid_format_signup_screen),
                    leadingIcon = {
                        Icon(
                            painter = getDrawableIllustration(IllustrationType.IC_PERSON_FILL),
                            contentDescription = null,
                            tint = ColorScheme.colorScheme.icon.default
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = {
                        action(
                            SignupAction.OnValueChange(
                                value = it,
                                type = SignupInputType.LAST_NAME
                            )
                        )
                    }
                )

                TextFieldUI(
                    value = state.email,
                    label = stringResource(R.string.label_input_email_signup_screen),
                    isError = state.inputError == SignupInputType.EMAIL,
                    error = stringResource(R.string.message_email_invalid_format_signup_screen),
                    leadingIcon = {
                        Icon(
                            painter = getDrawableIllustration(IllustrationType.IC_EMAIL_FILL),
                            contentDescription = null,
                            tint = ColorScheme.colorScheme.icon.default
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = {
                        action(
                            SignupAction.OnValueChange(
                                value = it,
                                type = SignupInputType.EMAIL
                            )
                        )
                    }
                )

                TextFieldPasswordUI(
                    value = state.password,
                    label = stringResource(R.string.label_input_password_signup_screen),
                    isError = state.inputError == SignupInputType.PASSWORD,
                    error = stringResource(R.string.message_password_invalid_format_signup_screen),
                    onValueChange = {
                        action(
                            SignupAction.OnValueChange(
                                value = it,
                                type = SignupInputType.PASSWORD
                            )
                        )
                    }
                )

                TermsOfUseUI(
                    checked = state.onTermsChecked,
                    isError = state.inputError == SignupInputType.TERMS,
                    onTermsClick = { openUrlInBrowser("https://valorizze.app/terms") },
                    onPrivacyClick = { openUrlInBrowser("https://valorizze.app/privacy-policy") },
                    onCheckedChange = {
                        action(SignupAction.OnTermsChange)
                    }
                )
            }

            state.sheetModel?.let { sheetModel ->
                DefaultBottomSheet(
                    onDismissRequest = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            action(SignupAction.ClearBottomSheet)
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
                                            action(SignupAction.ClearBottomSheet)
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
                                            action(SignupAction.ClearBottomSheet)
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
private fun SignupPreview() {
    HelloTheme {
        SignupContent(
            state = SignupState(
                email = "arley.santana@hellodev.com.br",
                password = "123456"
            ),
            action = {},
            navigateToValidateSignupScreen = {},
            onBackPressed = {}
        )
    }
}

