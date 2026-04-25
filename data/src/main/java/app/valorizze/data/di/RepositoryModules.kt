package app.valorizze.data.di

import app.valorizze.data.repository.remote.auth.AuthRepositoryImpl
import app.valorizze.data.repository.remote.confirmation.ConfirmationRepositoryImpl
import app.valorizze.data.repository.remote.user.UserRepositoryImpl
import app.valorizze.domain.repository.remote.auth.AuthRepository
import app.valorizze.domain.repository.remote.confirmation.ConfirmationRepository
import app.valorizze.domain.repository.remote.user.UserRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    factoryOf(::AuthRepositoryImpl).bind(AuthRepository::class)
    factoryOf(::UserRepositoryImpl).bind(UserRepository::class)
    factoryOf(::ConfirmationRepositoryImpl).bind(ConfirmationRepository::class)
}

