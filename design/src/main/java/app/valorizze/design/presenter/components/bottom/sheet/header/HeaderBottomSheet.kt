package app.valorizze.design.presenter.components.bottom.sheet.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import app.valorizze.core.enums.theme.ThemeType
import app.valorizze.design.provider.preview.LightDarkModePreviewProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.valorizze.design.presenter.components.bottom.sheet.drag.DragBottomSheet
import app.valorizze.design.presenter.components.divider.HorizontalDividerUI
import app.valorizze.design.presenter.theme.ColorScheme
import app.valorizze.design.presenter.theme.HelloTheme
import app.valorizze.design.presenter.theme.helloFontFamily
import androidx.compose.ui.res.stringResource
import app.valorizze.design.R

@Composable
fun HeaderBottomSheet(
    modifier: Modifier = Modifier,
    title: String?
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DragBottomSheet()

        Text(
            text = title ?: stringResource(R.string.text_tile_generic_sheet_content),
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 28.8.sp,
                fontFamily = helloFontFamily(),
                fontWeight = FontWeight(700),
                color = ColorScheme.colorScheme.text.primaryColor,
                textAlign = TextAlign.Center
            )
        )

        HorizontalDividerUI(
            modifier = Modifier
                .padding(vertical = 24.dp)
        )
    }
}

@Preview
@Composable
private fun HeaderBottomSheetPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType
) {
    HelloTheme(themeType = type) {
        HeaderBottomSheet(
            title = "Remover dos favoritos?"
        )
    }
}

