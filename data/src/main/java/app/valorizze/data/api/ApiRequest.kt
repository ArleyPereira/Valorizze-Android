package app.valorizze.data.api

import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.domain.model.base.BaseResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

class ApiRequest(val json: Json) {

    suspend inline operator fun <reified R : Any, D : Any> invoke(
        response: HttpResponse,
        noinline toDomain: (R) -> D
    ): BaseResponse<D> {
        return try {
            if (response.status.value in 200..299) {
                val responseBody = response.body<String>()
                val baseResponse = json.decodeFromString<BaseResponse<R>>(responseBody)
                    .copy(resultStatus = ResultStatus.SUCCESS)

                applyMapperSuccess(baseResponse, toDomain)
            } else {
                val errorBody = response.body<String>()
                val errorResponse = json.decodeFromString<BaseResponse<Unit>>(errorBody)

                if (response.status >= HttpStatusCode.InternalServerError) {
                    BaseResponse(
                        data = null,
                        status = response.status.value,
                        action = errorResponse.action,
                        resultStatus = ResultStatus.ERROR,
                        message = errorResponse.message
                            ?: "Por favor, tente novamente em alguns instantes."
                    )
                } else {
                    BaseResponse(
                        data = null,
                        status = response.status.value,
                        action = errorResponse.action,
                        resultStatus = ResultStatus.ERROR,
                        message = errorResponse.message
                    )
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            BaseResponse(
                data = null,
                status = response.status.value,
                action = null,
                resultStatus = ResultStatus.ERROR,
                message = "Por favor, tente novamente em alguns instantes."
            )
        }
    }

    fun <R, D> applyMapperSuccess(
        body: BaseResponse<R>,
        mapper: (R) -> D
    ): BaseResponse<D> {
        return BaseResponse(
            data = body.data?.let { mapper(it) },
            status = body.status,
            action = body.action,
            resultStatus = body.resultStatus,
            message = body.message
        )
    }
}

