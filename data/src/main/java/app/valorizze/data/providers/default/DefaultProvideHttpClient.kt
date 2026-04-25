package app.valorizze.data.providers.default

import app.valorizze.data.storage.preferences.LocalPreferences
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private enum class ApiEnvironment {
    DEVELOPMENT,
    PRODUCTION
}

private data class ApiEnvironmentConfig(
    val protocol: URLProtocol,
    val host: String,
    val port: Int? = null
)

private val CURRENT_API_ENVIRONMENT = ApiEnvironment.DEVELOPMENT

private val currentApiEnvironmentConfig: ApiEnvironmentConfig
    get() = when (CURRENT_API_ENVIRONMENT) {
        ApiEnvironment.PRODUCTION -> ApiEnvironmentConfig(
            protocol = URLProtocol.HTTPS,
            host = ""
        )

        ApiEnvironment.DEVELOPMENT -> ApiEnvironmentConfig(
            protocol = URLProtocol.HTTP,
            host = "192.168.70.7",
            port = 3001
        )
    }

fun defaultProvideHttpClient(
    localPreferences: LocalPreferences,
    json: Json
): HttpClient {
    return HttpClient(OkHttp) {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    // Android: pode trocar por Logcat depois
                    println(message)
                }
            }
            level = LogLevel.BODY
        }
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 20000
            connectTimeoutMillis = 20000
            socketTimeoutMillis = 20000
        }
        defaultRequest {
            url {
                val apiConfig = currentApiEnvironmentConfig
                protocol = apiConfig.protocol
                host = apiConfig.host
                apiConfig.port?.let { port = it }
                contentType(ContentType.Application.Json)
            }

            headers {
                localPreferences.getUser()?.token?.let { token ->
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }
    }
}

