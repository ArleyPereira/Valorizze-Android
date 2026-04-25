package app.valorizze.data.mapping.user

import app.valorizze.data.model.request.user.UserRequest
import app.valorizze.data.model.response.user.UserResponse
import app.valorizze.domain.dto.user.CreateUserDTO
import app.valorizze.domain.model.user.User

fun UserResponse.toDomain(): User {
    return User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        avatar = avatar,
        token = token,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun CreateUserDTO.toRequest(): UserRequest {
    return UserRequest(
        firstName = firstName,
        lastName = lastName,
        email = email,
        password = password
    )
}

