package app.valorizze.authentication.presenter.features.recover.email.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.valorizze.authentication.presenter.features.recover.email.action.RecoverEmailAction
import app.valorizze.authentication.presenter.features.recover.email.event.RecoverEmailEvent
import app.valorizze.authentication.presenter.features.recover.email.state.RecoverEmailState
import app.valorizze.core.enums.confirmation.ConfirmationType
import app.valorizze.core.enums.input.recover.RecoverInputType
import app.valorizze.core.enums.input.recover.RecoverInputType.EMAIL
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.core.functions.isValidEmail
import app.valorizze.domain.dto.confirmation.ConfirmationDTO
import app.valorizze.domain.model.sheet.DefaultSheetModel
import app.valorizze.domain.usecase.remote.confirmation.CreateConfirmationUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecoverEmailViewModel(
    private val createConfirmationUseCase: CreateConfirmationUseCase
) : ViewModel() {

    private var _state = MutableStateFlow(RecoverEmailState())
    var state: StateFlow<RecoverEmailState> = _state

    private var _event: Channel<RecoverEmailEvent> = Channel()
    var event = _event.receiveAsFlow()

    fun dispatchAction(action: RecoverEmailAction) {
        when (action) {
            is RecoverEmailAction.ClearBottomSheet -> {
                clearBottomSheet()
            }

            is RecoverEmailAction.CreateConfirmation -> {
                createConfirmation()
            }

            is RecoverEmailAction.OnValueChange -> {
                onValueChange(action.value, action.type)
            }
        }
    }

    private fun createConfirmation() {
        viewModelScope.launch {
            if (!isValidData()) {
                inputFeedbackError()
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val dto = ConfirmationDTO(
                email = _state.value.email,
                type = ConfirmationType.PASSWORD_RESET
            )

            val response = createConfirmationUseCase(dto)

            when (response.resultStatus) {
                ResultStatus.SUCCESS -> {
                    _state.update { it.copy(isLoading = false) }

                    _event.send(
                        RecoverEmailEvent.Navigation.RecoverCodeScreen(
                            email = _state.value.email,
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
            EMAIL -> {
                _state.update { it.copy(email = value.trim()) }
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
        return isValidEmail(_state.value.email)
    }

    private fun inputFeedbackError() {
        val inputError = when {
            !isValidEmail(_state.value.email) -> EMAIL
            else -> null
        }

        _state.update { it.copy(inputError = inputError) }
    }

    private fun clearBottomSheet() {
        _state.update { it.copy(sheetModel = null) }
    }

}

