package app.valorizze.authentication.presenter.features.recover.password.event

sealed class RecoverPasswordEvent {
    object Idle : RecoverPasswordEvent()

    sealed class Navigation {
        data class LoginScreen(
            val message: String = ""
        ) : RecoverPasswordEvent()
    }
}

