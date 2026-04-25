package app.valorizze.authentication.presenter.features.recover.email.event

sealed class RecoverEmailEvent {
    object Idle : RecoverEmailEvent()

    sealed class Navigation {
        data class RecoverCodeScreen(
            val email: String,
            val message: String
        ) : RecoverEmailEvent()
    }
}

