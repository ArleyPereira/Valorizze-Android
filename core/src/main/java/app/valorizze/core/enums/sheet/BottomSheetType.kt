package app.valorizze.core.enums.sheet

import app.valorizze.core.enums.action.ActionType

enum class BottomSheetType(val type: String) {
    THEME("THEME"),
    LOGOUT("LOGOUT"),
    GENERIC("GENERIC"),
    REMOVE_CARD("REMOVE_CARD"),
    SELECT_IMAGE("SELECT_IMAGE"),
    NOT_CONFIRMED("NOT_CONFIRMED"),
    SELECT_OPTIONS("SELECT_OPTIONS"),
    REMOVE_ACCOUNT("REMOVE_ACCOUNT"),
    SELECT_DUE_DAY("SELECT_DUE_DAY"),
    REVERT_PAYMENT("REVERT_PAYMENT"),
    ADD_TRANSACTION("ADD_TRANSACTION"),
    SELECT_CALENDAR("SELECT_CALENDAR"),
    BALANCE_ACCOUNT("BALANCE_ACCOUNT"),
    REMOVE_TRANSACTION("REMOVE_TRANSACTION"),
    SELECT_CLOSING_DAY("SELECT_CLOSING_DAY"),
    SELECT_TRANSACTION_METHOD("SELECT_TRANSACTION_METHOD");

    companion object {
        fun getType(action: ActionType?): BottomSheetType? {
            return BottomSheetType.entries.find { it.type == action?.action }
        }
    }
}

