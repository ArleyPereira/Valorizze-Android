package app.valorizze.authentication.presenter.features.login.event

sealed class LoginEvent {
    object Idle : LoginEvent()

    sealed class Navigation : LoginEvent() {
        object Main : Navigation()
    }
}

