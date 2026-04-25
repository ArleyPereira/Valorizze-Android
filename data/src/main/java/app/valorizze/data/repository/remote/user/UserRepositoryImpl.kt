package app.valorizze.data.repository.remote.user

import app.valorizze.core.enums.result.ResultStatus
import app.valorizze.data.api.ApiRequest
import app.valorizze.data.mapping.user.toDomain
import app.valorizze.data.mapping.user.toRequest
import app.valorizze.data.model.response.user.UserResponse
import app.valorizze.data.routes.users.UsersApiRoutes.USERS_ROUTE
import app.valorizze.domain.dto.user.CreateUserDTO
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.model.user.User
import app.valorizze.domain.repository.remote.user.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class UserRepositoryImpl(
    private val httpClient: HttpClient,
    private val apiRequest: ApiRequest,
) : UserRepository {

    override suspend fun create(dto: CreateUserDTO): BaseResponse<User> {
        return try {
            val response: HttpResponse = httpClient.post(USERS_ROUTE) {
                setBody(dto.toRequest())
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

