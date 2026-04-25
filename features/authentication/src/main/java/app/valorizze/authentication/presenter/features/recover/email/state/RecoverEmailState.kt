package app.valorizze.authentication.presenter.features.recover.email.state

import app.valorizze.core.enums.input.recover.RecoverInputType
import app.valorizze.domain.model.sheet.DefaultSheetModel

data class RecoverEmailState(
    val isLoading: Boolean = false,
    val email: String = "",
    val inputError: RecoverInputType? = null,
    val sheetModel: DefaultSheetModel? = null
)

