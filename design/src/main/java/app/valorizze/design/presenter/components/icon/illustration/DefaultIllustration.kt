package app.valorizze.design.presenter.components.icon.illustration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import app.valorizze.core.enums.illustration.IllustrationType
import app.valorizze.core.enums.illustration.IllustrationType.IC_ACCOUNT_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_ACCOUNT_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_ADD
import app.valorizze.core.enums.illustration.IllustrationType.IC_ADD_CARD_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_ADD_CARD_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_ADD_PHOTO_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_ADD_PHOTO_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_ALERT
import app.valorizze.core.enums.illustration.IllustrationType.IC_ARROW_CIRCLE_DOWN
import app.valorizze.core.enums.illustration.IllustrationType.IC_ARROW_DOWN
import app.valorizze.core.enums.illustration.IllustrationType.IC_ARROW_LEFT
import app.valorizze.core.enums.illustration.IllustrationType.IC_ARROW_NAV_RIGHT
import app.valorizze.core.enums.illustration.IllustrationType.IC_ARROW_RIGHT
import app.valorizze.core.enums.illustration.IllustrationType.IC_BALANCE_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_BALANCE_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_BAR_CHART
import app.valorizze.core.enums.illustration.IllustrationType.IC_CALENDAR
import app.valorizze.core.enums.illustration.IllustrationType.IC_CARD_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_CARD_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_CHECK
import app.valorizze.core.enums.illustration.IllustrationType.IC_CHECK_SIMPLE
import app.valorizze.core.enums.illustration.IllustrationType.IC_CLASSIFY
import app.valorizze.core.enums.illustration.IllustrationType.IC_CLOSE
import app.valorizze.core.enums.illustration.IllustrationType.IC_COPY
import app.valorizze.core.enums.illustration.IllustrationType.IC_CV_RESUME
import app.valorizze.core.enums.illustration.IllustrationType.IC_DARK_MODE_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_DARK_MODE_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_DELETE_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_DELETE_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_EDIT_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_EDIT_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_EDUCATION
import app.valorizze.core.enums.illustration.IllustrationType.IC_EMAIL_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_EMAIL_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_ERROR
import app.valorizze.core.enums.illustration.IllustrationType.IC_EXPECTED_SALARY
import app.valorizze.core.enums.illustration.IllustrationType.IC_FACEBOOK
import app.valorizze.core.enums.illustration.IllustrationType.IC_FILTER
import app.valorizze.core.enums.illustration.IllustrationType.IC_GITHUB
import app.valorizze.core.enums.illustration.IllustrationType.IC_GOOGLE
import app.valorizze.core.enums.illustration.IllustrationType.IC_HIDE
import app.valorizze.core.enums.illustration.IllustrationType.IC_HOME_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_HOME_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_INFO_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_INFO_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_LANGUAGES
import app.valorizze.core.enums.illustration.IllustrationType.IC_LIST
import app.valorizze.core.enums.illustration.IllustrationType.IC_LOCATION_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_LOCATION_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_LOCK
import app.valorizze.core.enums.illustration.IllustrationType.IC_LOGOUT
import app.valorizze.core.enums.illustration.IllustrationType.IC_MARK_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_MARK_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_MORE
import app.valorizze.core.enums.illustration.IllustrationType.IC_PDF
import app.valorizze.core.enums.illustration.IllustrationType.IC_PERSON_CANCEL
import app.valorizze.core.enums.illustration.IllustrationType.IC_PERSON_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_PERSON_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_PHONE_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_PHONE_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_PIN_FILL
import app.valorizze.core.enums.illustration.IllustrationType.IC_PIN_LINE
import app.valorizze.core.enums.illustration.IllustrationType.IC_PROJECTS
import app.valorizze.core.enums.illustration.IllustrationType.IC_RESET
import app.valorizze.core.enums.illustration.IllustrationType.IC_RIGHT
import app.valorizze.core.enums.illustration.IllustrationType.IC_SEARCH
import app.valorizze.core.enums.illustration.IllustrationType.IC_SEND
import app.valorizze.core.enums.illustration.IllustrationType.IC_SETTINGS
import app.valorizze.core.enums.illustration.IllustrationType.IC_SHOW
import app.valorizze.core.enums.illustration.IllustrationType.IC_SKILLS
import app.valorizze.core.enums.illustration.IllustrationType.IC_SUCCESS
import app.valorizze.core.enums.illustration.IllustrationType.IC_SUMMARY
import app.valorizze.core.enums.illustration.IllustrationType.IC_THUMB_DOWN
import app.valorizze.core.enums.illustration.IllustrationType.IC_THUMB_UP
import app.valorizze.core.enums.illustration.IllustrationType.IC_TRENDING_DOWN
import app.valorizze.core.enums.illustration.IllustrationType.IC_TRENDING_UP
import app.valorizze.core.enums.illustration.IllustrationType.IC_UPLOAD
import app.valorizze.core.enums.illustration.IllustrationType.IC_WORK_EXPERIENCE
import app.valorizze.core.enums.illustration.IllustrationType.LOGO
import app.valorizze.core.enums.illustration.IllustrationType.USER_PREVIEW
import app.valorizze.design.R

@Composable
fun getDrawableIllustration(
    type: IllustrationType
): Painter {
    return painterResource(
        when (type) {
            LOGO -> R.drawable.logo
            IC_FILTER -> R.drawable.ic_filter
            IC_EMAIL_FILL -> R.drawable.ic_email_fill
            IC_EMAIL_LINE -> R.drawable.ic_email_line
            IC_FACEBOOK -> R.drawable.ic_facebook
            IC_GOOGLE -> R.drawable.ic_google
            IC_GITHUB -> R.drawable.ic_github
            IC_HIDE -> R.drawable.ic_hide
            IC_SHOW -> R.drawable.ic_show
            IC_LOCK -> R.drawable.ic_lock
            IC_CLASSIFY -> R.drawable.ic_classify
            IC_SEARCH -> R.drawable.ic_search
            IC_EDIT_FILL -> R.drawable.ic_edit_fill
            IC_EDIT_LINE -> R.drawable.ic_edit_line
            IC_ADD -> R.drawable.ic_add
            IC_CALENDAR -> R.drawable.ic_calendar
            IC_RIGHT -> R.drawable.ic_right
            IC_MARK_FILL -> R.drawable.ic_mark_fill
            IC_MARK_LINE -> R.drawable.ic_mark_line
            IC_SEND -> R.drawable.ic_send
            IC_UPLOAD -> R.drawable.ic_upload
            IC_PDF -> R.drawable.ic_pdf
            IC_CLOSE -> R.drawable.ic_close
            IC_ERROR -> R.drawable.ic_error
            IC_SUCCESS -> R.drawable.ic_success
            IC_ARROW_LEFT -> R.drawable.ic_arrow_left
            IC_ARROW_RIGHT -> R.drawable.ic_arrow_right
            IC_ARROW_NAV_RIGHT -> R.drawable.ic_arrow_nav_right
            IC_ARROW_DOWN -> R.drawable.ic_arrow_down
            IC_PERSON_FILL -> R.drawable.ic_person_fill
            IC_PERSON_LINE -> R.drawable.ic_person_line
            IC_PHONE_LINE -> R.drawable.ic_phone_line
            IC_PHONE_FILL -> R.drawable.ic_phone_fill
            IC_LOCATION_LINE -> R.drawable.ic_location_line
            IC_LOCATION_FILL -> R.drawable.ic_location_fill
            IC_SUMMARY -> R.drawable.ic_summary
            IC_EXPECTED_SALARY -> R.drawable.ic_expected_salary
            IC_WORK_EXPERIENCE -> R.drawable.ic_work_experience
            IC_EDUCATION -> R.drawable.ic_education
            IC_PROJECTS -> R.drawable.ic_projects
            IC_LANGUAGES -> R.drawable.ic_languages
            IC_SKILLS -> R.drawable.ic_skills
            IC_CV_RESUME -> R.drawable.ic_cv_resume
            IC_SETTINGS -> R.drawable.ic_settings
            IC_ALERT -> R.drawable.ic_alert
            IC_CHECK -> R.drawable.ic_check
            IC_CHECK_SIMPLE -> R.drawable.ic_check_simple
            IC_INFO_FILL -> R.drawable.ic_info_fill
            IC_INFO_LINE -> R.drawable.ic_info_line
            IC_CARD_FILL -> R.drawable.ic_credit_card_fill
            IC_CARD_LINE -> R.drawable.ic_credit_card_line
            IC_LOGOUT -> R.drawable.ic_logout
            IC_DARK_MODE_FILL -> R.drawable.ic_dark_mode_fill
            IC_DARK_MODE_LINE -> R.drawable.ic_dark_mode_line
            IC_DELETE_FILL -> R.drawable.ic_delete_fill
            IC_DELETE_LINE -> R.drawable.ic_delete_line
            IC_ADD_CARD_FILL -> R.drawable.ic_add_card_fill
            IC_ADD_CARD_LINE -> R.drawable.ic_add_card_line
            IC_ADD_PHOTO_FILL -> R.drawable.ic_add_photo_fill
            IC_ADD_PHOTO_LINE -> R.drawable.ic_add_photo_line
            IC_ACCOUNT_FILL -> R.drawable.ic_account_fill
            IC_ACCOUNT_LINE -> R.drawable.ic_account_line
            USER_PREVIEW -> R.drawable.placeholder_photo_profile
            IC_TRENDING_DOWN -> R.drawable.ic_trending_down
            IC_TRENDING_UP -> R.drawable.ic_trending_up
            IC_BALANCE_FILL -> R.drawable.ic_balance_fill
            IC_BALANCE_LINE -> R.drawable.ic_balance_line
            IC_LIST -> R.drawable.ic_list
            IC_HOME_FILL -> R.drawable.ic_home_fill
            IC_HOME_LINE -> R.drawable.ic_home_line
            IC_BAR_CHART -> R.drawable.ic_bar_chart
            IC_ARROW_CIRCLE_DOWN -> R.drawable.ic_arrow_circle_down
            IC_MORE -> R.drawable.ic_more
            IC_COPY -> R.drawable.ic_copy
            IC_PIN_LINE -> R.drawable.ic_pin_line
            IC_PIN_FILL -> R.drawable.ic_pin_fill
            IC_THUMB_UP -> R.drawable.ic_thumb_up
            IC_THUMB_DOWN -> R.drawable.ic_thumb_down
            IC_RESET -> R.drawable.ic_reset
            IC_PERSON_CANCEL -> R.drawable.ic_person_cancel
        }
    )
}

@Composable
fun DefaultIcon(
    type: IllustrationType,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = Color.Unspecified,
    onClick: () -> Unit = {}
) {
    val painter = getDrawableIllustration(type = type)

    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        tint = tint
    )
}

