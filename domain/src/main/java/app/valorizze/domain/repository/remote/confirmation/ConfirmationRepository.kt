package app.valorizze.domain.repository.remote.confirmation

import app.valorizze.domain.dto.confirmation.ConfirmationDTO
import app.valorizze.domain.model.base.BaseResponse

interface ConfirmationRepository {
    suspend fun create(dto: ConfirmationDTO): BaseResponse<Unit>
    suspend fun resend(dto: ConfirmationDTO): BaseResponse<Unit>
    suspend fun validate(dto: ConfirmationDTO): BaseResponse<Unit>
    suspend fun confirm(dto: ConfirmationDTO): BaseResponse<Unit>
}

