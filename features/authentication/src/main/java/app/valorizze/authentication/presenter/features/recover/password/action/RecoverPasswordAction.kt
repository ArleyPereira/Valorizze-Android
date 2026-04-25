package app.valorizze.authentication.presenter.features.recover.password.action

import app.valorizze.core.enums.input.recover.RecoverInputType

sealed class RecoverPasswordAction {

    object ConfirmConfirmation : RecoverPasswordAction()
    object ClearBottomSheet : RecoverPasswordAction()

    data class OnValueChange(
        val value: String,
        val type: RecoverInputType
    ) : RecoverPasswordAction()

}

