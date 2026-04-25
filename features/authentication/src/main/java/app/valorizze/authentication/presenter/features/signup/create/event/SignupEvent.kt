package app.valorizze.authentication.presenter.features.signup.create.event

sealed class SignupEvent {
    object Idle : SignupEvent()

    sealed class Navigation {
        data class Validate(
            val email: String,
            val password: String,
            val message: String
        ) : SignupEvent()
    }
}

