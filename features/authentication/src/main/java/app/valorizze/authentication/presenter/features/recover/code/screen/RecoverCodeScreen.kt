package app.valorizze.authentication.presenter.features.recover.code.screen

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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.valorizze.authentication.presenter.features.recover.code.action.RecoverCodeAction
import app.valorizze.authentication.presenter.features.recover.code.event.RecoverCodeEvent
import app.valorizze.authentication.presenter.features.recover.code.state.RecoverCodeState
import app.valorizze.authentication.presenter.features.recover.code.viewmodel.RecoverCodeViewModel
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
import app.valorizze.design.presenter.theme.HelloTheme
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import app.valorizze.authentication.R

@Composable
fun RecoverCodeScreen(
    navigateToRecoverPasswordScreen: (email: String, code: String) -> Unit,
    onBackPressed: () -> Unit,
) {
    val viewModel = koinViewModel<RecoverCodeViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val event by viewModel.event.collectAsStateWithLifecycle(initialValue = RecoverCodeEvent.Idle)

    when (val currentEvent = event) {
        is RecoverCodeEvent.Navigation.RecoverPasswordScreen -> {
            navigateToRecoverPasswordScreen(
                currentEvent.email,
                currentEvent.code
            )
        }

        else -> {}
    }

    RecoverCodeContent(
        state = state,
        action = viewModel::dispatchAction,
        onBackPressed = onBackPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverCodeContent(
    state: RecoverCodeState,
    action: (RecoverCodeAction) -> Unit,
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
                            action(RecoverCodeAction.ValidateConfirmation)
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

                TextFieldUI(
                    modifier = Modifier,
                    value = state.code,
                    label = stringResource(R.string.label_code_recover_code_screen),
                    isError = state.inputError == RecoverInputType.CODE,
                    error = stringResource(R.string.message_code_invalid_format_recover_code_screen),
                    leadingIcon = {
                        Icon(
                            painter = getDrawableIllustration(IllustrationType.IC_PIN_FILL),
                            contentDescription = null,
                            tint = ColorScheme.colorScheme.icon.default
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = {
                        action(
                            RecoverCodeAction.OnValueChange(
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
                            action(RecoverCodeAction.ClearBottomSheet)
                        }
                    },
                    sheetState = sheetState,
                    content = {
                        GenericSheetContent(
                            sheet = sheetModel,
                            onFirstClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    action(RecoverCodeAction.ClearBottomSheet)
                                }
                            }
                        )
                    }
                )
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun RecoverCodePreview() {
    HelloTheme {
        RecoverCodeContent(
            state = RecoverCodeState(
                message = "Se o email informado existir em nossa base, um código será enviado para: arley@gmail.com",
                email = "arley@gmail.com"
            ),
            action = {},
            onBackPressed = {}
        )
    }
}

