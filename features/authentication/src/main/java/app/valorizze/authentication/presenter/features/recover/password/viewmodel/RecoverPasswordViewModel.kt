package app.valorizze.authentication.presenter.features.recover.password.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import app.valorizze.authentication.presenter.features.recover.password.action.RecoverPasswordAction
import app.valorizze.authentication.presenter.features.recover.password.event.RecoverPasswordEvent
import app.valorizze.authentication.presenter.features.recover.password.state.RecoverPasswordState
import app.valorizze.authentication.presenter.navigation.routes.AuthenticationRoutes
import app.valorizze.core.enums.confirmation.ConfirmationType
import app.valorizze.core.enums.input.recover.RecoverInputType
import app.valorizze.core.enums.input.recover.RecoverInputType.PASSWORD
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.domain.dto.confirmation.ConfirmationDTO
import app.valorizze.domain.model.sheet.DefaultSheetModel
import app.valorizze.domain.usecase.remote.confirmation.ConfirmConfirmationUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecoverPasswordViewModel(
    private val confirmConfirmationUseCase: ConfirmConfirmationUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _state = MutableStateFlow(RecoverPasswordState())
    var state: StateFlow<RecoverPasswordState> = _state

    private var _event: Channel<RecoverPasswordEvent> = Channel()
    var event = _event.receiveAsFlow()

    init {
        initData()
    }

    fun dispatchAction(action: RecoverPasswordAction) {
        when (action) {
            is RecoverPasswordAction.ClearBottomSheet -> {
                clearBottomSheet()
            }

            is RecoverPasswordAction.ConfirmConfirmation -> {
                confirmConfirmation()
            }

            is RecoverPasswordAction.OnValueChange -> {
                onValueChange(action.value, action.type)
            }
        }
    }

    private fun initData() {
        val email = savedStateHandle.toRoute<AuthenticationRoutes.RecoverPassword>().email
        val code = savedStateHandle.toRoute<AuthenticationRoutes.RecoverPassword>().code

        _state.update {
            it.copy(
                email = email,
                code = code
            )
        }
    }

    private fun confirmConfirmation() {
        viewModelScope.launch {
            if (!isValidData()) {
                inputFeedbackError()
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val dto = ConfirmationDTO(
                email = _state.value.email,
                code = _state.value.code,
                password = _state.value.password,
                type = ConfirmationType.PASSWORD_RESET
            )

            val response = confirmConfirmationUseCase(dto)

            when (response.resultStatus) {
                ResultStatus.SUCCESS -> {
                    _event.send(
                        RecoverPasswordEvent.Navigation.LoginScreen(
                            message = response.message.orEmpty()
                        )
                    )
                }

                else -> setupError(response.message)
            }
        }
    }

    private fun onValueChange(value: String, type: RecoverInputType) {
        when (type) {
            PASSWORD -> {
                _state.update { it.copy(password = value) }
            }

            else -> {}
        }

        _state.update { it.copy(inputError = null) }
    }

    private fun setupError(message: String?) {
        val sheetModel = DefaultSheetModel(message = message)

        _state.update {
            it.copy(
                sheetModel = sheetModel,
                isLoading = false
            )
        }
    }

    private fun isValidData(): Boolean {
        return _state.value.password.length >= 6
    }

    private fun inputFeedbackError() {
        val inputError = when {
            _state.value.password.length < 6 -> PASSWORD
            else -> null
        }

        _state.update { it.copy(inputError = inputError) }
    }

    private fun clearBottomSheet() {
        _state.update { it.copy(sheetModel = null) }
    }

}

