package app.valorizze.domain.model.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val avatar: String? = null,
    val token: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

