package app.valorizze.domain.di

import app.valorizze.domain.usecase.remote.auth.LoginUseCase
import app.valorizze.domain.usecase.remote.confirmation.ConfirmConfirmationUseCase
import app.valorizze.domain.usecase.remote.confirmation.CreateConfirmationUseCase
import app.valorizze.domain.usecase.remote.confirmation.ResendConfirmationUseCase
import app.valorizze.domain.usecase.remote.confirmation.ValidateConfirmationUseCase
import app.valorizze.domain.usecase.remote.user.CreateUserUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::LoginUseCase)
    factoryOf(::CreateUserUseCase)

    factoryOf(::CreateConfirmationUseCase)
    factoryOf(::ResendConfirmationUseCase)
    factoryOf(::ValidateConfirmationUseCase)
    factoryOf(::ConfirmConfirmationUseCase)
}

