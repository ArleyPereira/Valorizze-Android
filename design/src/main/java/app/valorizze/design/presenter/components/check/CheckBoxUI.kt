package app.valorizze.design.presenter.components.check

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.valorizze.design.extensions.modifier.shimmerPlaceholder
import app.valorizze.design.presenter.theme.BorderStrokeNone
import app.valorizze.design.presenter.theme.ColorScheme
import app.valorizze.design.presenter.theme.HelloTheme
import app.valorizze.design.presenter.theme.helloFontFamily
import androidx.compose.ui.res.painterResource
import app.valorizze.design.R

@Composable
fun CheckBoxUI(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    text: String = "",
    borderStroke: BorderStroke = BorderStrokeNone,
    onClick: () -> Unit
) {
    val painter = if (checked) {
        painterResource(R.drawable.ic_checked)
    } else {
        painterResource(R.drawable.ic_unchecked)
    }

    val textColor = if (enabled) {
        ColorScheme.colorScheme.text.primaryColor
    } else {
        ColorScheme.colorScheme.text.disabled
    }

    Row(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                enabled = enabled && !isLoading,
                onClick = onClick
            )
            .border(
                border = borderStroke,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            modifier = Modifier
                .size(20.dp)
                .shimmerPlaceholder(visible = isLoading),
            painter = painter,
            contentDescription = null
        )

        if (text.isNotEmpty()) {
            Text(
                text = text,
                modifier = Modifier
                    .shimmerPlaceholder(visible = isLoading),
                style = TextStyle(
                    lineHeight = 22.4.sp,
                    fontFamily = helloFontFamily(),
                    fontWeight = FontWeight(600),
                    color = textColor,
                    letterSpacing = 0.2.sp
                )
            )
        }
    }
}

@Preview
@Composable
private fun CheckBoxUIPreview() {
    var checked by remember { mutableStateOf(false) }

    HelloTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorScheme.colorScheme.screen.backgroundPrimary)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CheckBoxUI(
                checked = checked,
                text = "Accounting and Finance",
                onClick = {
                    checked = !checked
                }
            )
        }
    }
}

