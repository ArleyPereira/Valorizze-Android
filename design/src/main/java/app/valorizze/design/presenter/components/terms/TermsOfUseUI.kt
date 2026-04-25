package app.valorizze.design.presenter.components.terms

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.valorizze.core.enums.illustration.IllustrationType
import app.valorizze.core.enums.theme.ThemeType
import app.valorizze.design.presenter.components.check.CheckBoxUI
import app.valorizze.design.presenter.components.icon.illustration.getDrawableIllustration
import app.valorizze.design.presenter.theme.BorderStrokeNone
import app.valorizze.design.presenter.theme.ColorScheme
import app.valorizze.design.presenter.theme.HelloTheme
import app.valorizze.design.presenter.theme.borderStrokeDefault
import app.valorizze.design.presenter.theme.helloFontFamily
import app.valorizze.design.provider.preview.LightDarkModePreviewProvider
import androidx.compose.ui.res.stringResource
import app.valorizze.design.R

@Composable
fun TermsOfUseUI(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    isError: Boolean = false,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onCheckedChange: () -> Unit
) {
    val linkStyle = SpanStyle(
        color = ColorScheme.colorScheme.defaultColor,
        textDecoration = TextDecoration.Underline
    )

    val termsText = buildAnnotatedString {
        append(stringResource(R.string.terms_prefix))
        pushStringAnnotation(tag = "TERMS", annotation = "terms")
        pushStyle(linkStyle)
        append(stringResource(R.string.terms_link))
        pop()
        pop()
        append(stringResource(R.string.terms_middle))
        pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
        pushStyle(linkStyle)
        append(stringResource(R.string.privacy_link))
        pop()
        pop()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = ColorScheme.colorScheme.screen.backgroundSecondary
            ),
            border = if (isSystemInDarkTheme()) {
                BorderStrokeNone
            } else {
                borderStrokeDefault()
            },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp,
                        end = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckBoxUI(
                    checked = checked,
                    onClick = onCheckedChange
                )

                ClickableText(
                    text = termsText,
                    style = TextStyle(
                        fontFamily = helloFontFamily(),
                        color = ColorScheme.colorScheme.text.primaryColor,
                        letterSpacing = 0.2.sp
                    ),
                    onClick = { offset ->
                        termsText.getStringAnnotations(start = offset, end = offset)
                            .firstOrNull()?.let { annotation ->
                                when (annotation.tag) {
                                    "TERMS" -> onTermsClick()
                                    "PRIVACY" -> onPrivacyClick()
                                }
                            }
                    }
                )
            }
        }

        if (isError) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, top = 6.dp)
            ) {
                Icon(
                    painter = getDrawableIllustration(type = IllustrationType.IC_ALERT),
                    contentDescription = null,
                    tint = ColorScheme.colorScheme.alertColor
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.terms_error),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = helloFontFamily(),
                        color = ColorScheme.colorScheme.alertColor,
                        letterSpacing = 0.2.sp
                    )
                )
            }
        }
    }
}

@Composable
@Preview
fun TermsOfUseUIUIPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType
) {
    var isChecked by remember { mutableStateOf(false) }

    HelloTheme(themeType = type) {
        Column(
            modifier = Modifier
                .background(ColorScheme.colorScheme.screen.backgroundPrimary)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TermsOfUseUI(
                checked = isChecked,
                isError = false,
                onTermsClick = {},
                onPrivacyClick = {},
                onCheckedChange = { isChecked = !isChecked }
            )
        }
    }
}

