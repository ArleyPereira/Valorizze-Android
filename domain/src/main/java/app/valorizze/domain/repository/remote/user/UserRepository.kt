package app.valorizze.domain.repository.remote.user

import app.valorizze.domain.dto.user.CreateUserDTO
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.model.user.User

interface UserRepository {
    suspend fun create(dto: CreateUserDTO): BaseResponse<User>
}

