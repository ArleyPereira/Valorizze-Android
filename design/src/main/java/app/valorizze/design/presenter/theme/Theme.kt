package app.valorizze.design.presenter.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import app.valorizze.core.enums.theme.ThemeType
import app.valorizze.core.enums.theme.ThemeType.DARK
import app.valorizze.core.enums.theme.ThemeType.LIGHT
import app.valorizze.core.enums.theme.ThemeType.SYSTEM
import app.valorizze.design.presenter.ChangeSchemeColor
import app.valorizze.design.presenter.theme.scheme.BorderColorScheme
import app.valorizze.design.presenter.theme.scheme.ButtonColorScheme
import app.valorizze.design.presenter.theme.scheme.CheckColorScheme
import app.valorizze.design.presenter.theme.scheme.DividerColorScheme
import app.valorizze.design.presenter.theme.scheme.IconColorScheme
import app.valorizze.design.presenter.theme.scheme.MenuItemScheme
import app.valorizze.design.presenter.theme.scheme.MyColorScheme
import app.valorizze.design.presenter.theme.scheme.RadioColorScheme
import app.valorizze.design.presenter.theme.scheme.ScreenColorScheme
import app.valorizze.design.presenter.theme.scheme.ShimmerScheme
import app.valorizze.design.presenter.theme.scheme.SocialButtonColorScheme
import app.valorizze.design.presenter.theme.scheme.SwitchColorScheme
import app.valorizze.design.presenter.theme.scheme.TagColorScheme
import app.valorizze.design.presenter.theme.scheme.TextColorScheme
import app.valorizze.design.presenter.theme.scheme.TextFieldColorScheme
import app.valorizze.design.presenter.theme.scheme.TransactionColorScheme
import app.valorizze.design.presenter.theme.scheme.UploadColorScheme

private val LightColorScheme = MyColorScheme(
    text = TextColorScheme(
        primaryColor = PrimaryTextColorLight,
        secondaryColor = SecondaryTextColorLight,
        disabled = DisabledTextColorLight,
    ),
    screen = ScreenColorScheme(
        backgroundPrimary = PrimaryBackgroundColorLight,
        backgroundSecondary = SecondaryBackgroundColorLight,
    ),
    icon = IconColorScheme(
        color = IconColorLight,
        default = IconDefaultColor,
    ),
    button = ButtonColorScheme(
        primaryBackground = PrimaryButtonColor,
        secondaryBackground = SecondaryButtonColorLight,
        primaryText = PrimaryButtonTextColorLight,
        secondaryText = SecondaryButtonTextColorLight,
    ),
    border = BorderColorScheme(
        selected = SelectedBorderColor,
        unselected = UnselectedBorderColorLight,
    ),
    divider = DividerColorScheme(color = DividerColorLight),
    socialButton = SocialButtonColorScheme(
        background = BackgroundSocialButtonColorLight,
        border = BorderSocialButtonColorLight,
        text = TextSocialButtonColorLight,
    ),
    textField = TextFieldColorScheme(
        background = TextFieldBackgroundColorLight,
        text = TextFieldTextColorLight,
        placeholder = TextFieldPlaceholderColor,
        disabledText = DisabledTextFieldTextColorLight,
    ),
    menuItem = MenuItemScheme(background = MenuItemBackgroundColorLight),
    switch = SwitchColorScheme(
        checkedBackground = SwitchCheckedBackgroundColor,
        uncheckedBackground = SwitchUncheckedBackgroundColor,
        inactiveBackground = SwitchInactiveBackgroundColorLight,
    ),
    radio = RadioColorScheme(
        selectedColor = RadioSelectedColor,
        unselectedColor = RadioUnselectedColor,
    ),
    check = CheckColorScheme(
        checked = CheckedColor,
        unchecked = UncheckedColor,
    ),
    tag = TagColorScheme(
        background = TagBackgroundColorLight,
        text = TagTextColorLight,
        border = UnselectedBorderColorLight,
    ),
    upload = UploadColorScheme(
        background = UploadBackgroundLight,
        border = UploadBorderColorLight,
        text = UploadTextColorLight,
    ),
    shimmer = ShimmerScheme(
        background = BackgroundShimmerColorLight,
        highlight = HighlightShimmerColorLight,
    ),
    transaction = TransactionColorScheme(
        upTextColor = TransactionUpTextColorLight,
        downTextColor = TransactionDownTextColorLight,
        upIconColor = TransactionUpIconColorLight,
        downIconColor = TransactionDownIconColorLight,
        upBackgroundIconColor = TransactionUpBackgroundIconColorLight,
        downBackgroundIconColor = TransactionDownBackgroundIconColorLight,
        neutralBackgroundIconColor = TransactionNeutralBackgroundIconColorLight,
    ),
    defaultColor = DefaultColor,
    disabledDefaultColor = DisabledDefaultColor,
    alphaDefaultColor = AlphaDefaultColor,
    successColor = SuccessColor,
    alertColor = AlertColor,
    alertAlphaColor = AlertAlphaColor,
    warningColor = WarningColor,
    infoColor = InfoColor,
    disabledColor = DisabledColor,
    greyscale900Color = Greyscale900Color,
    greyscale800Color = Greyscale800Color,
    greyscale700Color = Greyscale700Color,
    greyscale600Color = Greyscale600Color,
    greyscale500Color = Greyscale500Color,
    greyscale400Color = Greyscale400Color,
    greyscale300Color = Greyscale300Color,
    greyscale200Color = Greyscale200Color,
    greyscale100Color = Greyscale100Color,
    greyscale50Color = Greyscale50Color,
    spotColor = SpotColor,
    ambientColor = AmbientColor,
)

private val DarkColorScheme = MyColorScheme(
    text = TextColorScheme(
        primaryColor = PrimaryTextColorDark,
        secondaryColor = SecondaryTextColorDark,
        disabled = DisabledTextColorDark,
    ),
    screen = ScreenColorScheme(
        backgroundPrimary = PrimaryBackgroundColorDark,
        backgroundSecondary = SecondaryBackgroundColorDark,
    ),
    icon = IconColorScheme(
        color = IconColorDark,
        default = IconDefaultColor,
    ),
    button = ButtonColorScheme(
        primaryBackground = PrimaryButtonColor,
        secondaryBackground = SecondaryButtonColorDark,
        primaryText = PrimaryButtonTextColorDark,
        secondaryText = SecondaryButtonTextColorDark,
    ),
    border = BorderColorScheme(
        selected = SelectedBorderColor,
        unselected = UnselectedBorderColorDark,
    ),
    divider = DividerColorScheme(color = DividerColorDark),
    socialButton = SocialButtonColorScheme(
        background = BackgroundSocialButtonColorDark,
        border = BorderSocialButtonColorDark,
        text = TextSocialButtonColorDark,
    ),
    textField = TextFieldColorScheme(
        background = TextFieldBackgroundColorDark,
        text = TextFieldTextColorDark,
        placeholder = TextFieldPlaceholderColor,
        disabledText = DisabledTextFieldTextColorDark,
    ),
    menuItem = MenuItemScheme(background = MenuItemBackgroundColorDark),
    switch = SwitchColorScheme(
        checkedBackground = SwitchCheckedBackgroundColor,
        uncheckedBackground = SwitchUncheckedBackgroundColor,
        inactiveBackground = SwitchInactiveBackgroundColorDark,
    ),
    radio = RadioColorScheme(
        selectedColor = RadioSelectedColor,
        unselectedColor = RadioUnselectedColor,
    ),
    check = CheckColorScheme(
        checked = CheckedColor,
        unchecked = UncheckedColor,
    ),
    tag = TagColorScheme(
        background = TagBackgroundColorDark,
        text = TagTextColorDark,
        border = UnselectedBorderColorDark,
    ),
    upload = UploadColorScheme(
        background = UploadBackgroundColorDark,
        border = UploadBorderColorDark,
        text = UploadTextColorDark,
    ),
    shimmer = ShimmerScheme(
        background = BackgroundShimmerColorDark,
        highlight = HighlightShimmerColorDark,
    ),
    transaction = TransactionColorScheme(
        upTextColor = TransactionUpTextColorDark,
        downTextColor = TransactionDownTextColorDark,
        upIconColor = TransactionUpIconColorDark,
        downIconColor = TransactionDownIconColorDark,
        upBackgroundIconColor = TransactionUpBackgroundIconColorDark,
        downBackgroundIconColor = TransactionDownBackgroundIconColorDark,
        neutralBackgroundIconColor = TransactionNeutralBackgroundIconColorDark,
    ),
    defaultColor = DefaultColor,
    disabledDefaultColor = DisabledDefaultColor,
    alphaDefaultColor = AlphaDefaultColor,
    successColor = SuccessColor,
    alertColor = AlertColor,
    alertAlphaColor = AlertAlphaColor,
    warningColor = WarningColor,
    infoColor = InfoColor,
    disabledColor = DisabledColor,
    greyscale900Color = Greyscale900Color,
    greyscale800Color = Greyscale800Color,
    greyscale700Color = Greyscale700Color,
    greyscale600Color = Greyscale600Color,
    greyscale500Color = Greyscale500Color,
    greyscale400Color = Greyscale400Color,
    greyscale300Color = Greyscale300Color,
    greyscale200Color = Greyscale200Color,
    greyscale100Color = Greyscale100Color,
    greyscale50Color = Greyscale50Color,
    spotColor = SpotColor,
    ambientColor = AmbientColor,
)

private val LocalColorScheme = compositionLocalOf { LightColorScheme }

object ColorScheme {
    val colorScheme: MyColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current
}

@Composable
fun HelloTheme(
    themeType: ThemeType = SYSTEM,
    content: @Composable () -> Unit,
) {
    val isDarkTheme = when (themeType) {
        LIGHT -> false
        DARK -> true
        SYSTEM -> isSystemInDarkTheme()
    }

    ChangeSchemeColor(isDarkTheme = isDarkTheme)

    val colorScheme by remember(isDarkTheme) {
        mutableStateOf(if (isDarkTheme) DarkColorScheme else LightColorScheme)
    }

    CompositionLocalProvider(LocalColorScheme provides colorScheme) {
        MaterialTheme(content = content)
    }
}

