package app.valorizze.design.presenter.components.bottom.sheet.body

import androidx.compose.runtime.Composable
import app.valorizze.core.enums.sheet.BottomSheetType
import app.valorizze.core.enums.sheet.BottomSheetType.NOT_CONFIRMED
import androidx.compose.ui.res.stringResource
import app.valorizze.design.R

@Composable
fun defaultFirstButtonText(type: BottomSheetType?): String {
    return when (type) {
        //NOT_FOUND -> stringResource(Res.string.text_first_button_user_not_registration_bottom_sheet)
        NOT_CONFIRMED -> stringResource(R.string.text_first_button_not_confirmed_sheet_content)
        //INVALID_CODE -> stringResource(Res.string.text_first_button_invalid_code_sheet_content)
        //INVALID_DATA -> stringResource(Res.string.text_first_button_invalid_data_sheet_content)
        //SEND_NEW_CODE_BY_EMAIL -> stringResource(Res.string.text_first_button_send_new_code_bottom_sheet)
        //EMAIL_ALREADY_REGISTERED -> stringResource(Res.string.text_first_button_email_already_registered_bottom_sheet)
        else -> stringResource(R.string.text_second_button_generic_sheet_content)
    }
}

@Composable
fun defaultSecondButtonText(type: BottomSheetType?): String {
    return when (type) {
        //NOT_FOUND -> stringResource(Res.string.text_second_button_user_not_registration_bottom_sheet)
        NOT_CONFIRMED -> stringResource(R.string.text_second_button_not_confirmed_sheet_content)
        //INVALID_CODE -> stringResource(Res.string.text_second_button_invalid_code_sheet_content)
        //INVALID_DATA -> stringResource(Res.string.text_second_button_invalid_data_sheet_content)
        //EMAIL_ALREADY_REGISTERED -> stringResource(Res.string.text_second_button_email_already_registered_bottom_sheet)
        else -> stringResource(R.string.text_first_button_generic_sheet_content)
    }
}

