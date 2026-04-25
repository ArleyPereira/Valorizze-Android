package app.valorizze.authentication.presenter.features.signup.validate.state

import app.valorizze.core.constants.Time.TIME_OTP_REQUEST
import app.valorizze.core.enums.input.recover.RecoverInputType
import app.valorizze.domain.model.feedback.Feedback
import app.valorizze.domain.model.sheet.DefaultSheetModel

data class ValidateSignupState(
    val isLoading: Boolean = false,
    val code: String = "",
    val email: String = "",
    val password: String = "",
    val message: String = "",
    val resendTimer: Int = TIME_OTP_REQUEST,
    val feedback: Feedback? = null,
    val inputError: RecoverInputType? = null,
    val sheetModel: DefaultSheetModel? = null
)

