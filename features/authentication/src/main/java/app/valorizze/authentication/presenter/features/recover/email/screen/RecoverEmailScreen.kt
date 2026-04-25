package app.valorizze.authentication.presenter.features.recover.email.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.valorizze.authentication.presenter.features.recover.email.action.RecoverEmailAction
import app.valorizze.authentication.presenter.features.recover.email.event.RecoverEmailEvent
import app.valorizze.authentication.presenter.features.recover.email.state.RecoverEmailState
import app.valorizze.authentication.presenter.features.recover.email.viewmodel.RecoverEmailViewModel
import app.valorizze.core.enums.illustration.IllustrationType
import app.valorizze.core.enums.input.recover.RecoverInputType
import app.valorizze.design.presenter.components.bar.top.TopAppBarUI
import app.valorizze.design.presenter.components.bottom.screen.BottomScreenUI
import app.valorizze.design.presenter.components.bottom.sheet.content.generic.GenericSheetContent
import app.valorizze.design.presenter.components.bottom.sheet.default.DefaultBottomSheet
import app.valorizze.design.presenter.components.button.PrimaryButton
import app.valorizze.design.presenter.components.header.HeaderScreen
import app.valorizze.design.presenter.components.icon.illustration.getDrawableIllustration
import app.valorizze.design.presenter.components.textfield.default.TextFieldUI
import app.valorizze.design.presenter.theme.ColorScheme
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import app.valorizze.authentication.R

@Composable
fun RecoverEmailScreen(
    navigateToRecoverCodeScreen: (email: String, message: String) -> Unit,
    onBackPressed: () -> Unit,
) {
    val viewModel = koinViewModel<RecoverEmailViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val event by viewModel.event.collectAsStateWithLifecycle(initialValue = RecoverEmailEvent.Idle)

    when (val currentState = event) {
        is RecoverEmailEvent.Navigation.RecoverCodeScreen -> {
            navigateToRecoverCodeScreen(
                currentState.email,
                currentState.message
            )
        }

        else -> {}
    }

    RecoverEmailContent(
        state = state,
        action = viewModel::dispatchAction,
        onBackPressed = onBackPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverEmailContent(
    state: RecoverEmailState,
    action: (RecoverEmailAction) -> Unit,
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
                title = stringResource(R.string.label_title_recover_flow),
                onBackPressed = onBackPressed
            )
        },
        bottomBar = {
            BottomScreenUI(
                content = {
                    PrimaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 24.dp
                            ),
                        text = stringResource(R.string.label_button_recover_flow),
                        isLoading = state.isLoading,
                        onClick = {
                            focusManager.clearFocus()
                            action(RecoverEmailAction.CreateConfirmation)
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
                HeaderScreen(
                    title = stringResource(R.string.label_sub_title_recover_email_screen)
                )

                TextFieldUI(
                    modifier = Modifier,
                    value = state.email,
                    label = stringResource(R.string.label_input_email_signup_screen),
                    isError = state.inputError == RecoverInputType.EMAIL,
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
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = {
                        action(
                            RecoverEmailAction.OnValueChange(
                                value = it,
                                type = RecoverInputType.EMAIL
                            )
                        )
                    }
                )
            }

            state.sheetModel?.let { sheetModel ->
                DefaultBottomSheet(
                    onDismissRequest = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            action(RecoverEmailAction.ClearBottomSheet)
                        }
                    },
                    sheetState = sheetState,
                    content = {
                        GenericSheetContent(
                            sheet = sheetModel,
                            onFirstClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    action(RecoverEmailAction.ClearBottomSheet)
                                }
                            }
                        )
                    }
                )
            }
        }
    )
}

