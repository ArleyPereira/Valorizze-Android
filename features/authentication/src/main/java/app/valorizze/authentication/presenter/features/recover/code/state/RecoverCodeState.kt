package app.valorizze.authentication.presenter.features.recover.code.state

import app.valorizze.core.enums.input.recover.RecoverInputType
import app.valorizze.domain.model.sheet.DefaultSheetModel

data class RecoverCodeState(
    val isLoading: Boolean = false,
    val code: String = "",
    val email: String = "",
    val message: String = "",
    val inputError: RecoverInputType? = null,
    val sheetModel: DefaultSheetModel? = null
)

