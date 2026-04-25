package app.valorizze.authentication.presenter.features.recover.code.event

sealed class RecoverCodeEvent {
    object Idle : RecoverCodeEvent()

    sealed class Navigation {
        data class RecoverPasswordScreen(
            val email: String = "",
            val code: String = ""
        ) : RecoverCodeEvent()
    }
}

