package app.valorizze.data.repository.remote.confirmation

import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.data.api.ApiRequest
import app.valorizze.data.mapping.confirmation.toRequest
import app.valorizze.data.routes.confirmations.ConfirmationsApiRoutes.CONFIRM
import app.valorizze.data.routes.confirmations.ConfirmationsApiRoutes.CREATE
import app.valorizze.data.routes.confirmations.ConfirmationsApiRoutes.RESEND
import app.valorizze.data.routes.confirmations.ConfirmationsApiRoutes.VALIDATE
import app.valorizze.domain.dto.confirmation.ConfirmationDTO
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.repository.remote.confirmation.ConfirmationRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class ConfirmationRepositoryImpl(
    private val httpClient: HttpClient,
    private val apiRequest: ApiRequest,
) : ConfirmationRepository {

    override suspend fun create(dto: ConfirmationDTO): BaseResponse<Unit> {
        return try {
            val response: HttpResponse = httpClient.post(CREATE) {
                setBody(dto.toRequest())
            }
            apiRequest<Unit, Unit>(response) { }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseResponse(
                data = null,
                status = null,
                resultStatus = ResultStatus.ERROR,
                message = "Por favor, tente novamente em alguns instantes."
            )
        }
    }

    override suspend fun resend(dto: ConfirmationDTO): BaseResponse<Unit> {
        return try {
            val response: HttpResponse = httpClient.post(RESEND) {
                setBody(dto.toRequest())
            }
            apiRequest<Unit, Unit>(response) { }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseResponse(
                data = null,
                status = null,
                resultStatus = ResultStatus.ERROR,
                message = "Por favor, tente novamente em alguns instantes."
            )
        }
    }

    override suspend fun validate(dto: ConfirmationDTO): BaseResponse<Unit> {
        return try {
            val response: HttpResponse = httpClient.post(VALIDATE) {
                setBody(dto.toRequest())
            }
            apiRequest<Unit, Unit>(response) { }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseResponse(
                data = null,
                status = null,
                resultStatus = ResultStatus.ERROR,
                message = "Por favor, tente novamente em alguns instantes."
            )
        }
    }

    override suspend fun confirm(dto: ConfirmationDTO): BaseResponse<Unit> {
        return try {
            val response: HttpResponse = httpClient.post(CONFIRM) {
                setBody(dto.toRequest())
            }
            apiRequest<Unit, Unit>(response) { }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseResponse(
                data = null,
                status = null,
                resultStatus = ResultStatus.ERROR,
                message = "Por favor, tente novamente em alguns instantes."
            )
        }
    }
}

