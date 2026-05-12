package app.valorizze.authentication.presenter.features.signup.validate.action

import app.valorizze.core.enums.input.recover.RecoverInputType
import app.valorizze.domain.model.feedback.Feedback

sealed class ValidateSignupAction {

    object ConfirmConfirmation : ValidateSignupAction()
    object ResendCode : ValidateSignupAction()
    object ClearBottomSheet : ValidateSignupAction()

    object DismissFeedback : ValidateSignupAction()

    data class CreateFeedback(
        val feedback: Feedback
    ) : ValidateSignupAction()

    data class OnValueChange(
        val value: String,
        val type: RecoverInputType
    ) : ValidateSignupAction()

}

