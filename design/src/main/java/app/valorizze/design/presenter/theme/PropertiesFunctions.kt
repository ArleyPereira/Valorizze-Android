package app.valorizze.design.presenter.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.valorizze.design.model.border.Border

// Border Stroke None
val BorderStrokeNone = BorderStroke(
    width = 0.dp,
    color = Color.Transparent
)

val ShapeBottomSheet = RoundedCornerShape(32.dp)

@Composable
fun Modifier.borderDefault(
    selected: Boolean = false,
    shape: Shape = CircleShape,
    color: Color = ColorScheme.colorScheme.border.selected
): Modifier {
    val border = if (selected) {
        Border(
            width = 2.dp,
            shape = shape,
            color = color
        )
    } else null

    return borderDefault(border = border)
}

@Composable
fun Modifier.borderDefault(
    border: Border?
): Modifier {
    if (border == null) return this

    return this
        .clip(border.shape)
        .drawWithContent {
            drawContent()
            drawOutline(
                outline = border.shape.createOutline(size, layoutDirection, this),
                color = border.color,
                style = Stroke(width = border.width.toPx())
            )
        }
}

@Composable
fun borderStrokeDefault(
    isSelect: Boolean = false,
    width: Dp = 1.dp,
    selectedColor: Color = ColorScheme.colorScheme.border.selected,
    unselectedColor: Color = ColorScheme.colorScheme.border.unselected
): BorderStroke {
    return if (isSelect) {
        BorderStroke(
            width = width,
            color = selectedColor
        )
    } else {
        BorderStroke(
            width = width,
            color = unselectedColor
        )
    }
}

@Composable
fun iconTintColor(
    filled: Boolean,
    isError: Boolean = false
): Color {
    return when {
        isError -> ColorScheme.colorScheme.alertColor
        filled -> ColorScheme.colorScheme.defaultColor
        else -> ColorScheme.colorScheme.icon.color
    }
}

