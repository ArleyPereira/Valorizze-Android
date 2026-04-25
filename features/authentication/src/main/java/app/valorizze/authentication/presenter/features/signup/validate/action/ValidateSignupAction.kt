package app.valorizze.authentication.presenter.features.signup.validate.action

import app.valorizze.core.enums.input.recover.RecoverInputType

sealed class ValidateSignupAction {

    object ConfirmConfirmation : ValidateSignupAction()
    object ResendCode : ValidateSignupAction()
    object ClearBottomSheet : ValidateSignupAction()

    object DismissFeedback : ValidateSignupAction()

    data class OnValueChange(
        val value: String,
        val type: RecoverInputType
    ) : ValidateSignupAction()

}

