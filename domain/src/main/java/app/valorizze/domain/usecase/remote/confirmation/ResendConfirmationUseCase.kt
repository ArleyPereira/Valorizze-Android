package app.valorizze.domain.usecase.remote.confirmation

import app.valorizze.domain.dto.confirmation.ConfirmationDTO
import app.valorizze.domain.repository.remote.confirmation.ConfirmationRepository

class ResendConfirmationUseCase(
    private val repository: ConfirmationRepository,
) {
    suspend operator fun invoke(dto: ConfirmationDTO) = repository.resend(dto)
}

