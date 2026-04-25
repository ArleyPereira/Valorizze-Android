package app.valorizze.data.di

import app.valorizze.data.api.ApiRequest
import app.valorizze.data.providers.default.defaultProvideHttpClient
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModules = module {

    single {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }
    }

    single<HttpClient> {
        defaultProvideHttpClient(
            localPreferences = get(),
            json = get(),
        )
    }

    single { ApiRequest(get()) }
}

