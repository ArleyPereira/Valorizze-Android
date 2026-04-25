package app.valorizze.data.model.request.confirmation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfirmationRequest(
    @SerialName("email")
    val email: String? = null,

    @SerialName("phone")
    val phone: String? = null,

    @SerialName("password")
    val password: String? = null,

    @SerialName("code")
    val code: String? = null,

    @SerialName("type")
    val type: String? = null,
)

