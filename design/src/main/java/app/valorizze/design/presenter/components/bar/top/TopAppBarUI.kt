package app.valorizze.design.presenter.components.bar.top

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import app.valorizze.core.enums.theme.ThemeType
import app.valorizze.design.presenter.theme.ColorScheme
import app.valorizze.design.presenter.theme.HelloTheme
import app.valorizze.design.presenter.theme.helloFontFamily
import app.valorizze.design.provider.preview.LightDarkModePreviewProvider
import androidx.compose.ui.res.painterResource
import app.valorizze.design.R

@ExperimentalMaterial3Api
@Composable
fun TopAppBarUI(
    modifier: Modifier = Modifier,
    navigationIcon: Painter = painterResource(R.drawable.ic_arrow_left),
    actions: @Composable (RowScope.() -> Unit) = {},
    title: String = "",
    centerAligned: Boolean = false,
    containerColor: Color = ColorScheme.colorScheme.screen.backgroundPrimary,
    onBackPressed: (() -> Unit)? = null
) {
    if (centerAligned) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    style = TextStyle(
                        lineHeight = 19.6.sp,
                        fontFamily = helloFontFamily(),
                        fontWeight = FontWeight(700),
                        color = ColorScheme.colorScheme.text.primaryColor
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            modifier = modifier,
            navigationIcon = {
                onBackPressed?.let {
                    IconButton(
                        onClick = it,
                        content = {
                            Icon(
                                painter = navigationIcon,
                                contentDescription = null,
                                tint = ColorScheme.colorScheme.icon.color
                            )
                        }
                    )
                }
            },
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = containerColor
            )
        )
    } else {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = TextStyle(
                        lineHeight = 19.6.sp,
                        fontFamily = helloFontFamily(),
                        fontWeight = FontWeight(700),
                        color = ColorScheme.colorScheme.text.primaryColor
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            modifier = modifier,
            navigationIcon = {
                onBackPressed?.let {
                    IconButton(
                        onClick = it,
                        content = {
                            Icon(
                                painter = navigationIcon,
                                contentDescription = null,
                                tint = ColorScheme.colorScheme.icon.color
                            )
                        }
                    )
                }
            },
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = containerColor
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TopAppBarUIPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType
) {
    HelloTheme(themeType = type) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorScheme.colorScheme.screen.backgroundPrimary),
        ) {
            TopAppBarUI(
                title = "Fill Your Profile",
                onBackPressed = {}
            )
        }
    }
}

