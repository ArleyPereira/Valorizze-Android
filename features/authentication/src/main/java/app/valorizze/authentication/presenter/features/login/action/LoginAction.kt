package app.valorizze.authentication.presenter.features.login.action

import app.valorizze.core.enums.input.login.LoginInputType

sealed class LoginAction {
    object OnSignIn : LoginAction()
    object DismissFeedback : LoginAction()
    object ClearBottomSheet : LoginAction()

    data class OnValueChange(
        val value: String,
        val type: LoginInputType,
    ) : LoginAction()
}

