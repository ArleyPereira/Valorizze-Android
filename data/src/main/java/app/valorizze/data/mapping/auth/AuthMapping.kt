package app.valorizze.data.mapping.auth

import app.valorizze.data.model.request.auth.LoginRequest
import app.valorizze.domain.dto.auth.LoginDTO

fun LoginDTO.toRequest(): LoginRequest {
    return LoginRequest(
        email = this.email,
        password = this.password
    )
}

