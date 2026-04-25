package app.valorizze.authentication.presenter.features.signup.validate.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.valorizze.authentication.presenter.features.signup.validate.action.ValidateSignupAction
import app.valorizze.authentication.presenter.features.signup.validate.event.ValidateSignupEvent
import app.valorizze.authentication.presenter.features.signup.validate.state.ValidateSignupState
import app.valorizze.authentication.presenter.features.signup.validate.viewmodel.ValidateSignupViewModel
import app.valorizze.core.enums.input.recover.RecoverInputType
import app.valorizze.core.enums.theme.ThemeType
import app.valorizze.design.presenter.components.bar.top.TopAppBarUI
import app.valorizze.design.presenter.components.bottom.screen.BottomScreenUI
import app.valorizze.design.presenter.components.bottom.sheet.content.generic.GenericSheetContent
import app.valorizze.design.presenter.components.bottom.sheet.default.DefaultBottomSheet
import app.valorizze.design.presenter.components.button.PrimaryButton
import app.valorizze.design.presenter.components.header.HeaderScreen
import app.valorizze.design.presenter.components.snackbar.FeedbackUI
import app.valorizze.design.presenter.components.textfield.default.TextFieldUI
import app.valorizze.design.presenter.components.textfield.otp.OTPTextFieldUI
import app.valorizze.design.presenter.theme.ColorScheme
import app.valorizze.design.presenter.theme.HelloTheme
import app.valorizze.design.provider.preview.LightDarkModePreviewProvider
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import app.valorizze.authentication.R

@Composable
fun ValidateSignupScreen(
    navigateToLoginScreen: (email: String, password: String, message: String) -> Unit,
    onBackPressed: () -> Unit,
) {
    val viewModel = koinViewModel<ValidateSignupViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val event by viewModel.event.collectAsStateWithLifecycle(initialValue = ValidateSignupEvent.Idle)

    when (val currentEvent = event) {
        is ValidateSignupEvent.Navigation.Login -> {
            navigateToLoginScreen(
                currentEvent.email,
                currentEvent.password,
                currentEvent.message
            )
        }

        else -> {}
    }

    ValidateSignupContent(
        state = state,
        action = viewModel::dispatchAction,
        onBackPressed = onBackPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidateSignupContent(
    state: ValidateSignupState,
    action: (ValidateSignupAction) -> Unit,
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
                title = stringResource(R.string.label_title_validate_signup_screen),
                onBackPressed = onBackPressed
            )
        },
        bottomBar = {
            BottomScreenUI(
                feedback = {
                    state.feedback?.let { feedback ->
                        FeedbackUI(
                            feedback = feedback,
                            onDismiss = { action(ValidateSignupAction.DismissFeedback) }
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
                        text = stringResource(R.string.label_button_validate_signup_screen),
                        isLoading = state.isLoading,
                        onClick = {
                            focusManager.clearFocus()
                            action(ValidateSignupAction.ConfirmConfirmation)
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
                HeaderScreen(title = state.message)

                OTPTextFieldUI(
                    modifier = Modifier,
                    code = state.code,
                    isError = state.inputError == RecoverInputType.CODE,
                    time = state.resendTimer,
                    resendCode = {
                        focusManager.clearFocus()
                        action(ValidateSignupAction.ResendCode)
                    },
                    onCodeChanged = {
                        action(
                            ValidateSignupAction.OnValueChange(
                                value = it,
                                type = RecoverInputType.CODE
                            )
                        )
                    }
                )
            }

            state.sheetModel?.let { sheetModel ->
                DefaultBottomSheet(
                    onDismissRequest = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            action(ValidateSignupAction.ClearBottomSheet)
                        }
                    },
                    sheetState = sheetState,
                    content = {
                        GenericSheetContent(
                            sheet = sheetModel,
                            onFirstClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    action(ValidateSignupAction.ClearBottomSheet)
                                }
                            }
                        )
                    }
                )
            }
        }
    )
}

@Preview
@Composable
private fun ValidateSignupPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType
) {
    HelloTheme(themeType = type) {
        ValidateSignupContent(
            state = ValidateSignupState(
                message = "Se o email informado existir em nossa base, um código será enviado para: arley@gmail.com",
                email = "arley@gmail.com",
                password = "123456"
            ),
            action = {},
            onBackPressed = {}
        )
    }
}

