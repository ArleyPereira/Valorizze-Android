package app.valorizze.authentication.fakes.respository.confirmation

import app.valorizze.domain.dto.confirmation.ConfirmationDTO
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.repository.remote.confirmation.ConfirmationRepository

class FakeConfirmationRepository : ConfirmationRepository {

    var createResponse: suspend (ConfirmationDTO) -> BaseResponse<Unit> =
        { error("create error") }

    var resendResponse: suspend (ConfirmationDTO) -> BaseResponse<Unit> =
        { error("resend error") }

    var validateResponse: suspend (ConfirmationDTO) -> BaseResponse<Unit> =
        { error("validate error") }

    var confirmResponse: suspend (ConfirmationDTO) -> BaseResponse<Unit> =
        { error("confirm error") }

    override suspend fun create(dto: ConfirmationDTO): BaseResponse<Unit> = createResponse(dto)

    override suspend fun resend(dto: ConfirmationDTO): BaseResponse<Unit> = resendResponse(dto)

    override suspend fun validate(dto: ConfirmationDTO): BaseResponse<Unit> = validateResponse(dto)

    override suspend fun confirm(dto: ConfirmationDTO): BaseResponse<Unit> = confirmResponse(dto)
}