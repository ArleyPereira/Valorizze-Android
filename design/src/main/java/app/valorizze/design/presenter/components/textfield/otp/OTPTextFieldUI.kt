package app.valorizze.design.presenter.components.textfield.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import app.valorizze.core.enums.illustration.IllustrationType
import app.valorizze.core.enums.theme.ThemeType
import app.valorizze.design.R
import app.valorizze.design.presenter.components.button.TextButtonUI
import app.valorizze.design.presenter.components.icon.illustration.getDrawableIllustration
import app.valorizze.design.presenter.components.textfield.default.TextFieldUI
import app.valorizze.design.presenter.theme.ColorScheme
import app.valorizze.design.presenter.theme.HelloTheme
import app.valorizze.design.provider.preview.LightDarkModePreviewProvider

@Composable
fun OTPTextFieldUI(
    modifier: Modifier = Modifier,
    code: String,
    isError: Boolean = false,
    time: Int,
    resendCode: () -> Unit,
    onCodeChanged: (String) -> Unit
) {
    val formattedTime = when {
        time >= 60 -> "${time / 60}:${(time % 60).toString().padStart(2, '0')}"
        else -> time.toString()
    }

    val displayText = if (time > 0) {
        stringResource(R.string.label_counting_code_opt_text_field, formattedTime)
    } else {
        stringResource(R.string.label_resend_code_opt_text_field)
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldUI(
            modifier = Modifier,
            value = code,
            label = stringResource(R.string.label_input_code_opt_text_field),
            isError = isError,
            error = stringResource(R.string.message_code_invalid_opt_text_field),
            leadingIcon = {
                Icon(
                    painter = getDrawableIllustration(IllustrationType.IC_PIN_FILL),
                    contentDescription = null,
                    tint = ColorScheme.colorScheme.icon.default
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            onValueChange = onCodeChanged
        )

        TextButtonUI(
            text = displayText,
            enabled = time == 0,
            onClick = {
                resendCode()
            }
        )
    }
}

@Preview
@Composable
private fun OTPTextFieldUIPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType
) {
    var otpValue by remember { mutableStateOf("1234") }

    HelloTheme(themeType = type) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorScheme.colorScheme.screen.backgroundPrimary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OTPTextFieldUI(
                code = otpValue,
                time = 90,
                resendCode = {},
                onCodeChanged = {
                    otpValue = it
                }
            )
        }
    }
}

