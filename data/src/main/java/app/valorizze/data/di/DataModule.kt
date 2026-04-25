package app.valorizze.data.di

import org.koin.dsl.module

val dataModule = module {
    includes(
        localModule,
        networkModules,
        repositoryModule,
    )
}

