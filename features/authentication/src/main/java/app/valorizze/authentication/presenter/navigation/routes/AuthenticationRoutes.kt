package app.valorizze.authentication.presenter.navigation.routes

import kotlinx.serialization.Serializable

sealed class AuthenticationRoutes {

    @Serializable
    object Graph : AuthenticationRoutes()

    @Serializable
    data class Login(
        val email: String = "",
        val password: String = "",
        val message: String = "",
    ) : AuthenticationRoutes()

    @Serializable
    object Signup : AuthenticationRoutes()

    @Serializable
    data class Validate(
        val email: String = "",
        val password: String = "",
        val message: String = "",
    ) : AuthenticationRoutes()

    @Serializable
    object RecoverEmail : AuthenticationRoutes()

    @Serializable
    data class RecoverCode(
        val email: String = "",
        val message: String = "",
    ) : AuthenticationRoutes()

    @Serializable
    data class RecoverPassword(
        val email: String = "",
        val code: String = "",
    ) : AuthenticationRoutes()
}

