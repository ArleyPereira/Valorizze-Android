package app.valorizze.design.extensions.modifier

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.valorizze.design.presenter.theme.ColorScheme
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.placeholder
import com.eygraber.compose.placeholder.shimmer

@Composable
fun Modifier.shimmerPlaceholder(
    visible: Boolean = false,
    color: Color = ColorScheme.colorScheme.shimmer.background,
    shape: RoundedCornerShape = RoundedCornerShape(4.dp),
    highlightColor: Color = ColorScheme.colorScheme.shimmer.highlight,
    animationDurationMillis: Int = 1000
): Modifier {
    return this.placeholder(
        visible = visible,
        color = color,
        shape = shape,
        highlight = PlaceholderHighlight.shimmer(
            highlightColor = highlightColor,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = animationDurationMillis),
                repeatMode = RepeatMode.Restart
            )
        )
    )
}

