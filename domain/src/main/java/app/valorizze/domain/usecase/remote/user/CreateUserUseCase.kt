package app.valorizze.domain.usecase.remote.user

import app.valorizze.domain.dto.user.CreateUserDTO
import app.valorizze.domain.repository.remote.user.UserRepository

class CreateUserUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(dto: CreateUserDTO) = repository.create(dto)
}

