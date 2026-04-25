package app.valorizze.domain.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserDTO(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)

