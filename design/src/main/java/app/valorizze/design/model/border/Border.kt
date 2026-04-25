package app.valorizze.design.model.border

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Border(
    val width: Dp = 1.dp,
    val shape: Shape,
    val color: Color
)

