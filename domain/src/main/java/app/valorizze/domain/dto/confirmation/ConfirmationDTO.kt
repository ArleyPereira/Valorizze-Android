package app.valorizze.domain.dto.confirmation

import app.valorizze.core.enums.confirmation.ConfirmationType

data class ConfirmationDTO(
    val email: String? = null,
    val phone: String? = null,
    val password: String? = null,
    val code: String? = null,
    val type: ConfirmationType? = null,
)

