package app.valorizze.authentication.di

import app.valorizze.authentication.presenter.features.login.viewmodel.LoginViewModel
import app.valorizze.authentication.presenter.features.recover.code.viewmodel.RecoverCodeViewModel
import app.valorizze.authentication.presenter.features.recover.email.viewmodel.RecoverEmailViewModel
import app.valorizze.authentication.presenter.features.recover.password.viewmodel.RecoverPasswordViewModel
import app.valorizze.authentication.presenter.features.signup.create.viewmodel.SignupViewModel
import app.valorizze.authentication.presenter.features.signup.validate.viewmodel.ValidateSignupViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authenticationModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::ValidateSignupViewModel)
    viewModelOf(::RecoverEmailViewModel)
    viewModelOf(::RecoverCodeViewModel)
    viewModelOf(::RecoverPasswordViewModel)
}

