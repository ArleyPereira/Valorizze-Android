package app.valorizze.design.presenter.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily

@Composable
fun helloFontFamily(): FontFamily {
    // Android: mantenha uma fonte padrão (no Multiplatform usa Compose Resources)
    return FontFamily.Default
}

