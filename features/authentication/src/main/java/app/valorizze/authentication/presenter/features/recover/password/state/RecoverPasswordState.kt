package app.valorizze.authentication.presenter.features.recover.password.state

import app.valorizze.core.enums.input.recover.RecoverInputType
import app.valorizze.domain.model.sheet.DefaultSheetModel

data class RecoverPasswordState(
    val isLoading: Boolean = false,
    val email: String = "",
    val code: String = "",
    val password: String = "",
    val inputError: RecoverInputType? = null,
    val sheetModel: DefaultSheetModel? = null
)

