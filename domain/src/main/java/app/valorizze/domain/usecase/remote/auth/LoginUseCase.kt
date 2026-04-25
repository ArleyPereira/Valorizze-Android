package app.valorizze.domain.usecase.remote.auth

import app.valorizze.domain.dto.auth.LoginDTO
import app.valorizze.domain.repository.remote.auth.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(loginDTO: LoginDTO) = repository.login(loginDTO)
}

