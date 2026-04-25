package app.valorizze.design.presenter.components.button

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.valorizze.design.presenter.components.loading.CircularProgressLoading
import app.valorizze.design.presenter.theme.ColorScheme
import app.valorizze.design.presenter.theme.HelloTheme
import app.valorizze.design.presenter.theme.helloFontFamily

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = ColorScheme.colorScheme.button.primaryBackground,
        disabledContainerColor = ColorScheme.colorScheme.disabledDefaultColor
    ),
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(58.dp),
        enabled = enabled && !isLoading,
        colors = colors,
        content = {
            if (isLoading) {
                CircularProgressLoading(
                    modifier = Modifier
                        .size(32.dp),
                    color = Color.White
                )
            } else {
                Text(
                    text = text,
                    style = TextStyle(
                        lineHeight = 22.4.sp,
                        fontFamily = helloFontFamily(),
                        fontWeight = FontWeight.Bold,
                        color = ColorScheme.colorScheme.button.primaryText,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.2.sp
                    )
                )
            }
        }
    )
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
    HelloTheme {
        PrimaryButton(
            text = "Continuar",
            isLoading = false,
            enabled = true,
            onClick = {}
        )
    }
}
