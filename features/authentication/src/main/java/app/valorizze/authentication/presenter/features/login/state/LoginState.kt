package app.valorizze.authentication.presenter.features.login.state

import app.valorizze.core.enums.input.login.LoginInputType
import app.valorizze.domain.model.feedback.Feedback
import app.valorizze.domain.model.sheet.DefaultSheetModel

data class LoginState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val message: String = "",
    val feedback: Feedback? = null,
    val inputError: LoginInputType? = null,
    val sheetModel: DefaultSheetModel? = null,
)

