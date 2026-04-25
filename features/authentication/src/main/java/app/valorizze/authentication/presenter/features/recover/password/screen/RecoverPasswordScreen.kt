package app.valorizze.authentication.presenter.features.recover.password.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.valorizze.authentication.presenter.features.recover.password.action.RecoverPasswordAction
import app.valorizze.authentication.presenter.features.recover.password.event.RecoverPasswordEvent
import app.valorizze.authentication.presenter.features.recover.password.state.RecoverPasswordState
import app.valorizze.authentication.presenter.features.recover.password.viewmodel.RecoverPasswordViewModel
import app.valorizze.core.enums.input.recover.RecoverInputType
import app.valorizze.design.presenter.components.bar.top.TopAppBarUI
import app.valorizze.design.presenter.components.bottom.screen.BottomScreenUI
import app.valorizze.design.presenter.components.bottom.sheet.content.generic.GenericSheetContent
import app.valorizze.design.presenter.components.bottom.sheet.default.DefaultBottomSheet
import app.valorizze.design.presenter.components.button.PrimaryButton
import app.valorizze.design.presenter.components.header.HeaderScreen
import app.valorizze.design.presenter.components.textfield.password.TextFieldPasswordUI
import app.valorizze.design.presenter.theme.ColorScheme
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import app.valorizze.authentication.R

@Composable
fun RecoverPasswordScreen(
    navigateToLoginScreen: (message: String) -> Unit,
    onBackPressed: () -> Unit,
) {
    val viewModel = koinViewModel<RecoverPasswordViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val event by viewModel.event.collectAsStateWithLifecycle(initialValue = RecoverPasswordEvent.Idle)

    when (val currentEvent = event) {
        is RecoverPasswordEvent.Navigation.LoginScreen -> {
            navigateToLoginScreen(currentEvent.message)
        }

        else -> {}
    }

    RecoverPasswordContent(
        state = state,
        action = viewModel::dispatchAction,
        onBackPressed = onBackPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverPasswordContent(
    state: RecoverPasswordState,
    action: (RecoverPasswordAction) -> Unit,
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
                        text = stringResource(R.string.label_button_recover_password_screen),
                        isLoading = state.isLoading,
                        onClick = {
                            focusManager.clearFocus()
                            action(RecoverPasswordAction.ConfirmConfirmation)
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
                HeaderScreen(title = stringResource(R.string.label_sub_title_recover_password_screen))

                TextFieldPasswordUI(
                    value = state.password,
                    label = stringResource(R.string.label_code_recover_password_screen),
                    isError = state.inputError == RecoverInputType.PASSWORD,
                    error = stringResource(R.string.message_password_invalid_format_recover_password_screen),
                    onValueChange = {
                        action(
                            RecoverPasswordAction.OnValueChange(
                                value = it,
                                type = RecoverInputType.PASSWORD
                            )
                        )
                    }
                )
            }

            state.sheetModel?.let { sheetModel ->
                DefaultBottomSheet(
                    onDismissRequest = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            action(RecoverPasswordAction.ClearBottomSheet)
                        }
                    },
                    sheetState = sheetState,
                    content = {
                        GenericSheetContent(
                            sheet = sheetModel,
                            onFirstClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    action(RecoverPasswordAction.ClearBottomSheet)
                                }
                            }
                        )
                    }
                )
            }
        }
    )
}

