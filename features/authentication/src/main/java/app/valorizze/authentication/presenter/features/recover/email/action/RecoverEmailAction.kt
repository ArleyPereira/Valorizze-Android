package app.valorizze.authentication.presenter.features.recover.email.action

import app.valorizze.core.enums.input.recover.RecoverInputType

sealed class RecoverEmailAction {

    object CreateConfirmation : RecoverEmailAction()
    object ClearBottomSheet : RecoverEmailAction()

    data class OnValueChange(
        val value: String,
        val type: RecoverInputType
    ) : RecoverEmailAction()

}

