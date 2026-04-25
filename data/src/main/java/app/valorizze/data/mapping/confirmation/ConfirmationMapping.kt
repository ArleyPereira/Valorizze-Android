package app.valorizze.data.mapping.confirmation

import app.valorizze.data.model.request.confirmation.ConfirmationRequest
import app.valorizze.domain.dto.confirmation.ConfirmationDTO

fun ConfirmationDTO.toRequest(): ConfirmationRequest {
    return ConfirmationRequest(
        email = email,
        phone = phone,
        password = password,
        code = code,
        type = type?.name
    )
}

