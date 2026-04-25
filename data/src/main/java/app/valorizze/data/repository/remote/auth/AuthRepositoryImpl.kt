package app.valorizze.data.repository.remote.auth

import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.data.api.ApiRequest
import app.valorizze.data.mapping.auth.toRequest
import app.valorizze.data.mapping.user.toDomain
import app.valorizze.data.model.response.user.UserResponse
import app.valorizze.data.routes.auth.AuthApiRoutes.LOGIN_AUTH_ROUTE
import app.valorizze.domain.dto.auth.LoginDTO
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.model.user.User
import app.valorizze.domain.repository.remote.auth.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val apiRequest: ApiRequest,
) : AuthRepository {
    override suspend fun login(loginDTO: LoginDTO): BaseResponse<User> {
        return try {
            val response: HttpResponse = httpClient.post(LOGIN_AUTH_ROUTE) {
                setBody(loginDTO.toRequest())
            }

            apiRequest<UserResponse, User>(response) { it.toDomain() }
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

