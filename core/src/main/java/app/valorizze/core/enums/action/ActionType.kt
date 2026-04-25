package app.valorizze.core.enums.action

import kotlinx.serialization.Serializable

@Serializable
enum class ActionType(val action: String) {

    NOT_CONFIRMED("NOT_CONFIRMED"),
    SEND_NEW_CODE_BY_EMAIL("send_new_code_by_email"),
    INVALID_DATA("invalid_data"),
    AUTHORIZED_ACCESS("authorized_access"),
    NOT_FOUND("not_found"),
    EMAIL_ALREADY_REGISTERED("email_already_registered");

    companion object {
        fun getActionType(action: String?): ActionType? {
            return entries.find { it.action == action }
        }
    }

}

