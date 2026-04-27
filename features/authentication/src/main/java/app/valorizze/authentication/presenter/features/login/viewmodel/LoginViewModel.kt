package app.valorizze.authentication.presenter.features.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.valorizze.authentication.presenter.features.login.action.LoginAction
import app.valorizze.authentication.presenter.features.login.state.LoginState
import app.valorizze.core.enums.action.ActionType
import app.valorizze.core.enums.feedback.FeedbackType
import app.valorizze.core.enums.input.login.LoginInputType
import app.valorizze.core.enums.input.login.LoginInputType.EMAIL
import app.valorizze.core.enums.input.login.LoginInputType.PASSWORD
import app.valorizze.core.enums.result.ResultStatus.SUCCESS
import app.valorizze.core.enums.sheet.BottomSheetType
import app.valorizze.core.functions.isValidEmail
import app.valorizze.data.storage.preferences.LocalPreferences
import app.valorizze.domain.dto.auth.LoginDTO
import app.valorizze.domain.model.feedback.Feedback
import app.valorizze.domain.model.sheet.DefaultSheetModel
import app.valorizze.domain.usecase.remote.auth.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val localPreferences: LocalPreferences,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun dispatchAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnValueChange -> {
                onValueChange(action.value, action.type)
            }

            LoginAction.OnSignIn -> {
                onSignIn()
            }

            LoginAction.DismissFeedback -> {
                dismissFeedback()
            }

            LoginAction.ClearBottomSheet -> {
                clearBottomSheet()
            }
        }
    }

    private fun onSignIn() {
        viewModelScope.launch {
            val inputError = validateInputs()
            if (inputError != null) {
                _state.update { it.copy(inputError = inputError) }
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val response = loginUseCase(
                LoginDTO(
                    email = _state.value.email,
                    password = _state.value.password,
                )
            )

            when (response.resultStatus) {
                SUCCESS -> {
                    localPreferences.saveUser(user = response.data)
                    setFeedback(
                        feedback = Feedback(
                            title = response.message.orEmpty(),
                            type = FeedbackType.SUCCESS
                        )
                    )
                }

                else -> setupError(message = response.message, action = response.action)
            }
        }
    }

    private fun onValueChange(value: String, type: LoginInputType) {
        when (type) {
            EMAIL -> _state.update { it.copy(email = value.trim()) }
            PASSWORD -> _state.update { it.copy(password = value) }
        }
        _state.update { it.copy(inputError = null) }
    }

    private fun validateInputs(): LoginInputType? {
        val s = _state.value
        return when {
            !isValidEmail(s.email) -> EMAIL
            s.password.isEmpty() -> PASSWORD
            else -> null
        }
    }

    private fun setupError(message: String?, action: String?) {
        val type = ActionType.getActionType(action)
        val sheetModel = DefaultSheetModel(
            type = BottomSheetType.getType(type),
            message = message,
        )

        _state.update {
            it.copy(
                sheetModel = sheetModel,
                message = message.orEmpty(),
                isLoading = false
            )
        }
    }

    private fun setFeedback(feedback: Feedback) {
        _state.update {
            it.copy(
                feedback = feedback,
                isLoading = false
            )
        }
    }

    private fun dismissFeedback() {
        _state.update { it.copy(feedback = null) }
    }

    private fun clearBottomSheet() {
        _state.update { it.copy(sheetModel = null) }
    }

}

