package app.valorizze.authentication.presenter.features.recover.code.action

import app.valorizze.core.enums.input.recover.RecoverInputType

sealed class RecoverCodeAction {

    object ValidateConfirmation : RecoverCodeAction()
    object ClearBottomSheet : RecoverCodeAction()

    data class OnValueChange(
        val value: String,
        val type: RecoverInputType
    ) : RecoverCodeAction()

}

