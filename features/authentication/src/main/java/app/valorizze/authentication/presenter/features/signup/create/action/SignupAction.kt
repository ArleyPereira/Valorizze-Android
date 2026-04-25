package app.valorizze.authentication.presenter.features.signup.create.action

import app.valorizze.core.enums.input.signup.SignupInputType

sealed class SignupAction {

    object OnPasswordVisibilityChange : SignupAction()

    object CreateUser : SignupAction()

    object DismissFeedback : SignupAction()

    object ClearBottomSheet : SignupAction()

    object OnTermsChange : SignupAction()

    data class OnValueChange(
        val value: String,
        val type: SignupInputType
    ) : SignupAction()
}

