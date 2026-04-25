package app.valorizze.design.presenter.components.textfield.password

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.valorizze.core.enums.illustration.IllustrationType
import app.valorizze.design.presenter.components.icon.illustration.getDrawableIllustration
import app.valorizze.design.presenter.theme.ColorScheme
import app.valorizze.design.presenter.theme.HelloTheme
import app.valorizze.design.presenter.theme.helloFontFamily
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

@Composable
fun TextFieldPasswordUI(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String = "",
    enabled: Boolean = true,
    isError: Boolean = false,
    error: String = "",
    maxLength: Int = Int.MAX_VALUE,
    requireKeyboardFocus: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done
    ),
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = ColorScheme.colorScheme.defaultColor,
        backgroundColor = ColorScheme.colorScheme.alphaDefaultColor
    )

    var showPassword by remember { mutableStateOf(false) }

    val visualTransformation = if (showPassword) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    val borderColor = if (isFocused && isError) {
        ColorScheme.colorScheme.alertColor
    } else if (isFocused) {
        ColorScheme.colorScheme.defaultColor
    } else if (isError) {
        ColorScheme.colorScheme.alertColor
    } else {
        ColorScheme.colorScheme.border.unselected
    }

    val backgroundColor = if (isFocused && isError) {
        ColorScheme.colorScheme.alertAlphaColor
    } else if (isFocused) {
        ColorScheme.colorScheme.alphaDefaultColor
    } else if (isError) {
        ColorScheme.colorScheme.alertAlphaColor
    } else {
        ColorScheme.colorScheme.textField.background
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            TextField(
                value = value,
                onValueChange = {
                    if (it.length <= maxLength) {
                        onValueChange(it)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
                    .focusRequester(focusRequester),
                enabled = enabled,
                label = {
                    Text(
                        fontFamily = helloFontFamily(),
                        text = label,
                        style = if (isFocused) {
                            TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 11.sp,
                                fontFamily = helloFontFamily(),
                                fontWeight = FontWeight(700),
                                color = ColorScheme.colorScheme.defaultColor
                            )
                        } else {
                            TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 15.4.sp,
                                fontFamily = helloFontFamily(),
                                color = ColorScheme.colorScheme.textField.placeholder
                            )
                        }
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = getDrawableIllustration(type = IllustrationType.IC_LOCK),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { showPassword = !showPassword },
                        content = {
                            Icon(
                                painter = if (showPassword) {
                                    getDrawableIllustration(type = IllustrationType.IC_HIDE)
                                } else getDrawableIllustration(type = IllustrationType.IC_SHOW),
                                contentDescription = null,
                                tint = ColorScheme.colorScheme.icon.color
                            )
                        }
                    )
                },
                isError = isError,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = backgroundColor,
                    focusedContainerColor = backgroundColor,
                    disabledContainerColor = backgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorContainerColor = ColorScheme.colorScheme.alertAlphaColor,
                    errorIndicatorColor = Color.Transparent,
                    unfocusedTextColor = ColorScheme.colorScheme.textField.text,
                    focusedTextColor = ColorScheme.colorScheme.textField.text,
                    errorTextColor = ColorScheme.colorScheme.textField.text,
                    disabledTextColor = ColorScheme.colorScheme.textField.disabledText,
                    cursorColor = ColorScheme.colorScheme.defaultColor
                )
            )
        }

        if (isError) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, top = 6.dp)
            ) {
                Icon(
                    painter = getDrawableIllustration(type = IllustrationType.IC_ALERT),
                    contentDescription = null,
                    tint = ColorScheme.colorScheme.alertColor
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = error,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 19.6.sp,
                        fontFamily = helloFontFamily(),
                        color = ColorScheme.colorScheme.alertColor,
                        letterSpacing = 0.2.sp
                    )
                )
            }
        }

        if (requireKeyboardFocus) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}

@Preview
@Composable
private fun TextFieldPasswordUIPreview() {
    var textValue by remember {
        mutableStateOf("testando")
    }

    HelloTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorScheme.colorScheme.screen.backgroundPrimary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextFieldPasswordUI(
                modifier = Modifier
                    .padding(32.dp),
                value = textValue,
                label = "Senha",
                onValueChange = {
                    textValue = it
                }
            )
        }
    }
}
