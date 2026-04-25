package app.valorizze.authentication.presenter.features.signup.create.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.valorizze.authentication.presenter.features.signup.create.action.SignupAction
import app.valorizze.authentication.presenter.features.signup.create.event.SignupEvent
import app.valorizze.authentication.presenter.features.signup.create.state.SignupState
import app.valorizze.core.enums.action.ActionType
import app.valorizze.core.enums.input.signup.SignupInputType
import app.valorizze.core.enums.input.signup.SignupInputType.EMAIL
import app.valorizze.core.enums.input.signup.SignupInputType.FIRST_NAME
import app.valorizze.core.enums.input.signup.SignupInputType.LAST_NAME
import app.valorizze.core.enums.input.signup.SignupInputType.PASSWORD
import app.valorizze.core.enums.input.signup.SignupInputType.TERMS
import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.core.enums.sheet.BottomSheetType
import app.valorizze.core.functions.capitalizeEachWord
import app.valorizze.core.functions.isValidEmail
import app.valorizze.core.functions.isValidName
import app.valorizze.domain.dto.user.CreateUserDTO
import app.valorizze.domain.model.sheet.DefaultSheetModel
import app.valorizze.domain.usecase.remote.user.CreateUserUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignupViewModel(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SignupState())
    val state = _state.asStateFlow()

    private var _event: Channel<SignupEvent> = Channel()
    var event = _event.receiveAsFlow()

    fun submitAction(action: SignupAction) {
        when (action) {
            is SignupAction.OnValueChange -> {
                onValueChange(action.value, action.type)
            }

            is SignupAction.OnPasswordVisibilityChange -> {
                onPasswordVisibilityChange()
            }

            is SignupAction.CreateUser -> {
                createUser()
            }

            is SignupAction.DismissFeedback -> {
                dismissFeedback()
            }

            is SignupAction.ClearBottomSheet -> {
                clearBottomSheet()
            }

            is SignupAction.OnTermsChange -> {
                onTermsChange()
            }
        }
    }

    private fun createUser() {
        viewModelScope.launch {
            if (!isValidData()) {
                inputFeedbackError()
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val dto = CreateUserDTO(
                firstName = _state.value.firstName,
                lastName = _state.value.lastName,
                email = _state.value.email,
                password = _state.value.password
            )

            val response = createUserUseCase(dto)

            when (response.resultStatus) {
                ResultStatus.SUCCESS -> {
                    _state.update { it.copy(isLoading = false) }

                    _event.send(
                        SignupEvent.Navigation.Validate(
                            email = _state.value.email,
                            password = _state.value.password,
                            message = response.message.orEmpty()
                        )
                    )
                }

                else -> setupError(message = response.message, action = response.action)
            }
        }
    }

    private fun onValueChange(value: String, type: SignupInputType) {
        when (type) {
            FIRST_NAME -> {
                _state.update { it.copy(firstName = capitalizeEachWord(value).trim()) }
            }

            LAST_NAME -> {
                _state.update { it.copy(lastName = capitalizeEachWord(value).trim()) }
            }

            EMAIL -> {
                _state.update { it.copy(email = value.trim()) }
            }

            PASSWORD -> {
                _state.update { it.copy(password = value) }
            }

            else -> {}
        }

        _state.update { it.copy(inputError = null) }
    }

    private fun onTermsChange() {
        _state.update { it.copy(onTermsChecked = !it.onTermsChecked) }
    }

    private fun setupError(message: String?, action: String? = null) {
        val type = ActionType.getActionType(action)

        val sheetModel = DefaultSheetModel(
            type = BottomSheetType.getType(type),
            message = message
        )

        _state.update {
            it.copy(
                sheetModel = sheetModel,
                message = message.orEmpty(),
                isLoading = false
            )
        }
    }

    private fun isValidData(): Boolean {
        val firstName = isValidName(_state.value.firstName)
        val surName = isValidName(_state.value.lastName)
        val email = isValidEmail(_state.value.email)
        val password = _state.value.password.isNotEmpty()
        val onTermsChecked = _state.value.onTermsChecked

        return firstName && surName && email && password && onTermsChecked
    }

    private fun inputFeedbackError() {
        val inputError = when {
            !isValidName(_state.value.firstName) -> FIRST_NAME
            !isValidName(_state.value.lastName) -> LAST_NAME
            !isValidEmail(_state.value.email) -> EMAIL
            _state.value.password.isEmpty() -> PASSWORD
            !_state.value.onTermsChecked -> TERMS
            else -> null
        }

        _state.update { it.copy(inputError = inputError) }
    }

    private fun onPasswordVisibilityChange() {
        _state.update { currentState ->
            currentState.copy(passwordVisibility = !currentState.passwordVisibility)
        }
    }

    private fun dismissFeedback() {
        _state.update { it.copy(feedback = null) }
    }

    private fun clearBottomSheet() {
        _state.update { it.copy(sheetModel = null) }
    }

}

