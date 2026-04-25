package app.valorizze.authentication.presenter.features.signup.create.state

import app.valorizze.core.enums.input.signup.SignupInputType
import app.valorizze.domain.model.feedback.Feedback
import app.valorizze.domain.model.sheet.DefaultSheetModel

data class SignupState(
    val isLoading: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val onTermsChecked: Boolean = false,
    val message: String = "",
    val passwordVisibility: Boolean = false,
    val feedback: Feedback? = null,
    val inputError: SignupInputType? = null,
    val sheetModel: DefaultSheetModel? = null,
)

