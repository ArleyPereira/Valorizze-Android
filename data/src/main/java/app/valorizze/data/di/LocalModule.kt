package app.valorizze.data.di

import app.valorizze.data.storage.preferences.LocalPreferences
import org.koin.dsl.module

val localModule = module {
    single { LocalPreferences() }
}

