package app.valorizze.design.presenter.components.bottom.sheet.content.generic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import app.valorizze.core.enums.theme.ThemeType
import app.valorizze.design.provider.preview.LightDarkModePreviewProvider
import androidx.compose.ui.unit.dp
import app.valorizze.core.enums.sheet.BottomSheetOrientationType
import app.valorizze.design.presenter.components.bottom.sheet.body.BodyBottomSheet
import app.valorizze.design.presenter.components.bottom.sheet.body.defaultFirstButtonText
import app.valorizze.design.presenter.components.bottom.sheet.body.defaultSecondButtonText
import app.valorizze.design.presenter.components.bottom.sheet.header.HeaderBottomSheet
import app.valorizze.design.presenter.components.button.PrimaryButton
import app.valorizze.design.presenter.components.button.SecondaryButton
import app.valorizze.design.presenter.components.spacer.HorizontalSpacer
import app.valorizze.design.presenter.components.spacer.VerticalSpacer
import app.valorizze.design.presenter.theme.ColorScheme
import app.valorizze.design.presenter.theme.HelloTheme
import app.valorizze.design.presenter.theme.ShapeBottomSheet
import app.valorizze.domain.model.sheet.DefaultSheetModel

@Composable
fun GenericSheetContent(
    modifier: Modifier = Modifier,
    sheet: DefaultSheetModel,
    orientation: BottomSheetOrientationType = BottomSheetOrientationType.VERTICAL,
    onFirstClick: () -> Unit,
    onSecondClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 32.dp
            )
            .background(ColorScheme.colorScheme.screen.backgroundSecondary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderBottomSheet(title = sheet.title)

        BodyBottomSheet(message = sheet.message.orEmpty())

        when (orientation) {
            BottomSheetOrientationType.VERTICAL -> {
                onSecondClick?.let {
                    SecondaryButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = sheet.secondButtonText ?: defaultSecondButtonText(type = sheet.type),
                        onClick = it
                    )

                    VerticalSpacer(size = 12)
                }

                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = sheet.firstButtonText ?: defaultFirstButtonText(type = sheet.type),
                    onClick = onFirstClick
                )
            }

            BottomSheetOrientationType.HORIZONTAL -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    onSecondClick?.let {
                        SecondaryButton(
                            modifier = Modifier
                                .weight(1f),
                            text = sheet.secondButtonText ?: defaultSecondButtonText(type = sheet.type),
                            onClick = it
                        )

                        HorizontalSpacer(size = 12)
                    }

                    PrimaryButton(
                        modifier = Modifier
                            .weight(1f),
                        text = sheet.firstButtonText ?: defaultFirstButtonText(type = sheet.type),
                        onClick = onFirstClick
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GenericSheetPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType
) {
    HelloTheme(themeType = type) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorScheme.colorScheme.screen.backgroundPrimary),
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = ColorScheme.colorScheme.screen.backgroundSecondary,
                        shape = ShapeBottomSheet
                    )
            ) {
                GenericSheetContent(
                    sheet = DefaultSheetModel(
                        title = "Não foi possível efetuar o login",
                        message = "Por favor, tente novamente em alguns instantes.",
                        firstButtonText = "Tentar novamente",
                        secondButtonText = "Ok, entendi"
                    ),
                    onFirstClick = {},
                    onSecondClick = {}
                )
            }
        }
    }
}

