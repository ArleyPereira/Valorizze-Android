package app.valorizze.domain.model.sheet

import app.valorizze.core.enums.sheet.BottomSheetType

data class DefaultSheetModel(
    val type: BottomSheetType? = null,
    val message: String? = "",
    val title: String? = null,
    val firstButtonText: String? = null,
    val secondButtonText: String? = null,
)

