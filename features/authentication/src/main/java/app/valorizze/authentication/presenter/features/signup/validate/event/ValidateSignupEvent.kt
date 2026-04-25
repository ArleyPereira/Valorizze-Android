package app.valorizze.authentication.presenter.features.signup.validate.event

sealed class ValidateSignupEvent {
    object Idle : ValidateSignupEvent()

    sealed class Navigation {
        data class Login(
            val email: String = "",
            val password: String = "",
            val message: String = ""
        ) : ValidateSignupEvent()
    }
}

