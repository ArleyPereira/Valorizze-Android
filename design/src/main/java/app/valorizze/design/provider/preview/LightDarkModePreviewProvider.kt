package app.valorizze.design.provider.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.valorizze.core.enums.theme.ThemeType

class LightDarkModePreviewProvider : PreviewParameterProvider<ThemeType> {
    override val values: Sequence<ThemeType>
        get() = sequenceOf(ThemeType.LIGHT, ThemeType.DARK)
}

