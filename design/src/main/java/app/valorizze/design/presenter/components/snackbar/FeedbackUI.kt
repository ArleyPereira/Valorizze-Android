package app.valorizze.design.presenter.components.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.valorizze.core.constants.Time.FEED_BACK_DISMISS_TIME
import app.valorizze.core.enums.feedback.FeedbackType.ERROR
import app.valorizze.core.enums.feedback.FeedbackType.INFO
import app.valorizze.core.enums.feedback.FeedbackType.SUCCESS
import app.valorizze.core.enums.feedback.FeedbackType.WARNING
import app.valorizze.core.enums.illustration.IllustrationType
import app.valorizze.core.enums.theme.ThemeType
import app.valorizze.design.presenter.components.icon.illustration.getDrawableIllustration
import app.valorizze.design.presenter.theme.ColorScheme
import app.valorizze.design.presenter.theme.HelloTheme
import app.valorizze.design.presenter.theme.helloFontFamily
import app.valorizze.design.provider.preview.LightDarkModePreviewProvider
import app.valorizze.domain.model.feedback.Feedback
import kotlinx.coroutines.delay

@Composable
fun FeedbackUI(
    modifier: Modifier = Modifier,
    feedback: Feedback,
    onDismiss: () -> Unit
) {
    val containerColor = when (feedback.type) {
        SUCCESS -> ColorScheme.colorScheme.successColor
        INFO -> ColorScheme.colorScheme.infoColor
        WARNING -> ColorScheme.colorScheme.warningColor
        ERROR -> ColorScheme.colorScheme.alertColor
    }

    val iconPainter = when (feedback.type) {
        SUCCESS -> getDrawableIllustration(type = IllustrationType.IC_CHECK)
        else -> getDrawableIllustration(type = IllustrationType.IC_INFO_LINE)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = iconPainter,
                contentDescription = null,
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
            ) {
                Text(
                    text = feedback.title,
                    style = TextStyle(
                        fontFamily = helloFontFamily(),
                        fontWeight = FontWeight(700),
                        color = Color.White,
                        letterSpacing = 0.2.sp
                    )
                )

                feedback.message?.let { message ->
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = message,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = helloFontFamily(),
                            fontWeight = FontWeight(300),
                            color = Color.White,
                            letterSpacing = 0.2.sp
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(FEED_BACK_DISMISS_TIME)
        onDismiss()
    }
}

@Preview
@Composable
private fun FeedbackUIPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType
) {
    HelloTheme(themeType = type) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorScheme.colorScheme.screen.backgroundPrimary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FeedbackUI(
                feedback = Feedback(
                    title = "Login efetuado com sucesso",
                    type = SUCCESS
                ),
                onDismiss = {}
            )

            FeedbackUI(
                feedback = Feedback(
                    title = "Login efetuado com sucesso",
                    type = INFO
                ),
                onDismiss = {}
            )

            FeedbackUI(
                feedback = Feedback(
                    title = "Login efetuado com sucesso",
                    type = WARNING
                ),
                onDismiss = {}
            )

            FeedbackUI(
                feedback = Feedback(
                    title = "Login efetuado com sucesso",
                    type = ERROR
                ),
                onDismiss = {}
            )
        }
    }
}

