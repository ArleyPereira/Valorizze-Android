package app.valorizze.authentication.presenter.features.signup.validate.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import app.valorizze.authentication.presenter.features.signup.validate.action.ValidateSignupAction
import app.valorizze.authentication.presenter.features.signup.validate.event.ValidateSignupEvent
import app.valorizze.authentication.presenter.features.signup.validate.state.ValidateSignupState
import app.valorizze.authentication.presenter.navigation.routes.AuthenticationRoutes
import app.valorizze.core.constants.Time.TIME_OTP_REQUEST
import app.valorizze.core.enums.confirmation.ConfirmationType
import app.valorizze.core.enums.feedback.FeedbackType
import app.valorizze.core.enums.input.recover.RecoverInputType
import app.valorizze.core.enums.input.recover.RecoverInputType.CODE
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.domain.dto.confirmation.ConfirmationDTO
import app.valorizze.domain.model.feedback.Feedback
import app.valorizze.domain.model.sheet.DefaultSheetModel
import app.valorizze.domain.usecase.remote.confirmation.ConfirmConfirmationUseCase
import app.valorizze.domain.usecase.remote.confirmation.ResendConfirmationUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ValidateSignupViewModel(
    private val confirmConfirmationUseCase: ConfirmConfirmationUseCase,
    private val resendConfirmationUseCase: ResendConfirmationUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _state = MutableStateFlow(ValidateSignupState())
    var state: StateFlow<ValidateSignupState> = _state

    private var _event: Channel<ValidateSignupEvent> = Channel()
    var event = _event.receiveAsFlow()

    init {
        initData()
    }

    fun dispatchAction(action: ValidateSignupAction) {
        when (action) {
            is ValidateSignupAction.ClearBottomSheet -> {
                clearBottomSheet()
            }

            is ValidateSignupAction.ConfirmConfirmation -> {
                confirmConfirmation()
            }

            is ValidateSignupAction.OnValueChange -> {
                onValueChange(action.value, action.type)
            }

            is ValidateSignupAction.ResendCode -> {
                resendConfirmation()
            }

            is ValidateSignupAction.DismissFeedback -> {
                dismissFeedback()
            }

            is ValidateSignupAction.CreateFeedback -> {
                createFeedback(action.feedback)
            }
        }
    }

    private fun initData() {
        val email = savedStateHandle.toRoute<AuthenticationRoutes.Validate>().email
        val password = savedStateHandle.toRoute<AuthenticationRoutes.Validate>().password
        val message = savedStateHandle.toRoute<AuthenticationRoutes.Validate>().message

        _state.update {
            it.copy(
                email = email,
                password = password,
                message = message
            )
        }

        startTimer()
    }

    private fun confirmConfirmation() {
        viewModelScope.launch {
            if (!isValidData()) {
                inputFeedbackError()
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val dto = ConfirmationDTO(
                type = ConfirmationType.REGISTER,
                email = _state.value.email,
                code = _state.value.code
            )

            val response = confirmConfirmationUseCase(dto)

            when (response.resultStatus) {
                ResultStatus.SUCCESS -> {
                    _event.send(
                        ValidateSignupEvent.Navigation.Login(
                            email = _state.value.email,
                            password = _state.value.password,
                            message = response.message.orEmpty()
                        )
                    )
                }

                else -> setupError(response.message)
            }
        }
    }

    private fun resendConfirmation() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    inputError = null,
                    resendTimer = TIME_OTP_REQUEST,
                    isLoading = true
                )
            }

            startTimer()

            val dto = ConfirmationDTO(
                email = _state.value.email,
                type = ConfirmationType.PASSWORD_RESET
            )

            val response = resendConfirmationUseCase(dto)

            when (response.resultStatus) {
                ResultStatus.SUCCESS -> {
                    createFeedback(
                        feedback = Feedback(
                            title = response.message.orEmpty(),
                            type = FeedbackType.SUCCESS
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

    private fun startTimer() {
        viewModelScope.launch {
            while (_state.value.resendTimer > 0) {
                delay(1000L)
                _state.update { it.copy(resendTimer = it.resendTimer - 1) }
            }
        }
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

    private fun createFeedback(feedback: Feedback) {
        _state.update {
            it.copy(isLoading = false, feedback = feedback)
        }
    }

    private fun dismissFeedback() {
        _state.update { it.copy(feedback = null) }
    }

    private fun clearBottomSheet() {
        _state.update { it.copy(sheetModel = null) }
    }

}

