package app.valorizze.di

import app.valorizze.authentication.di.authenticationModule
import app.valorizze.data.di.dataModule
import app.valorizze.domain.di.domainModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null,
) {
    startKoin {
        config?.invoke(this)
        modules(
            domainModule,
            dataModule,
            authenticationModule,
        )
    }
}

