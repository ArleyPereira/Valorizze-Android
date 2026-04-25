package app.valorizze.domain.repository.remote.auth

import app.valorizze.domain.dto.auth.LoginDTO
import app.valorizze.domain.model.base.BaseResponse
import app.valorizze.domain.model.user.User

interface AuthRepository {
    suspend fun login(loginDTO: LoginDTO): BaseResponse<User>
}

