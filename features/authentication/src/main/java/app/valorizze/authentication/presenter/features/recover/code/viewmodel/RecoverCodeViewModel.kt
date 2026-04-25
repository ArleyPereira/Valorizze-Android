package app.valorizze.authentication.presenter.features.recover.code.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import app.valorizze.authentication.presenter.features.recover.code.action.RecoverCodeAction
import app.valorizze.authentication.presenter.features.recover.code.event.RecoverCodeEvent
import app.valorizze.authentication.presenter.features.recover.code.state.RecoverCodeState
import app.valorizze.authentication.presenter.navigation.routes.AuthenticationRoutes
import app.valorizze.core.enums.confirmation.ConfirmationType
import app.valorizze.core.enums.input.recover.RecoverInputType
import app.valorizze.core.enums.input.recover.RecoverInputType.CODE
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.domain.dto.confirmation.ConfirmationDTO
import app.valorizze.domain.model.sheet.DefaultSheetModel
import app.valorizze.domain.usecase.remote.confirmation.ValidateConfirmationUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecoverCodeViewModel(
    private val validateConfirmationUseCase: ValidateConfirmationUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _state = MutableStateFlow(RecoverCodeState())
    var state: StateFlow<RecoverCodeState> = _state

    private var _event: Channel<RecoverCodeEvent> = Channel()
    var event = _event.receiveAsFlow()

    init {
        initData()
    }

    fun dispatchAction(action: RecoverCodeAction) {
        when (action) {
            is RecoverCodeAction.ClearBottomSheet -> {
                clearBottomSheet()
            }

            is RecoverCodeAction.OnValueChange -> {
                onValueChange(action.value, action.type)
            }

            is RecoverCodeAction.ValidateConfirmation -> {
                validateConfirmation()
            }
        }
    }

    private fun initData() {
        val email = savedStateHandle.toRoute<AuthenticationRoutes.RecoverCode>().email
        val message = savedStateHandle.toRoute<AuthenticationRoutes.RecoverCode>().message

        _state.update {
            it.copy(
                email = email,
                message = message
            )
        }
    }

    private fun validateConfirmation() {
        viewModelScope.launch {
            if (!isValidData()) {
                inputFeedbackError()
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val dto = ConfirmationDTO(
                email = _state.value.email,
                code = _state.value.code,
                type = ConfirmationType.PASSWORD_RESET
            )

            val response = validateConfirmationUseCase(dto)

            when (response.resultStatus) {
                ResultStatus.SUCCESS -> {
                    _state.update { it.copy(isLoading = false) }

                    _event.send(
                        RecoverCodeEvent.Navigation.RecoverPasswordScreen(
                            email = _state.value.email,
                            code = _state.value.code
                        )
                    )
                }

                else -> setupError(response.message)
            }
        }
    }

    private fun onValueChange(value: String, type: RecoverInputType) {
        when (type) {
            CODE -> {
                _state.update { it.copy(code = value) }
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
        return _state.value.code.isNotEmpty()
    }

    private fun inputFeedbackError() {
        val inputError = when {
            _state.value.code.isEmpty() -> CODE
            else -> null
        }

        _state.update { it.copy(inputError = inputError) }
    }

    private fun clearBottomSheet() {
        _state.update { it.copy(sheetModel = null) }
    }

}

