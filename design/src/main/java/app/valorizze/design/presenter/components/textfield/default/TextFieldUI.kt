package app.valorizze.design.presenter.components.textfield.default

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.valorizze.core.enums.illustration.IllustrationType
import app.valorizze.design.presenter.components.icon.illustration.getDrawableIllustration
import app.valorizze.design.presenter.theme.ColorScheme
import app.valorizze.design.presenter.theme.HelloTheme
import app.valorizze.design.presenter.theme.helloFontFamily
import app.valorizze.core.mask.MaskVisualTransformation
import androidx.compose.ui.res.painterResource
import app.valorizze.design.R
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

@Composable
fun TextFieldUI(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    error: String = "",
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    showMaxLength: Boolean = false,
    maxLength: Int = Int.MAX_VALUE,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    requireKeyboardFocus: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = ColorScheme.colorScheme.defaultColor,
        backgroundColor = ColorScheme.colorScheme.alphaDefaultColor
    )

    val borderColor = when {
        isFocused && isError -> ColorScheme.colorScheme.alertColor
        isFocused -> ColorScheme.colorScheme.defaultColor
        isError -> ColorScheme.colorScheme.alertColor
        else -> ColorScheme.colorScheme.border.unselected
    }

    val backgroundColor = when {
        isFocused && isError -> ColorScheme.colorScheme.alertAlphaColor
        isFocused -> ColorScheme.colorScheme.alphaDefaultColor
        isError -> ColorScheme.colorScheme.alertAlphaColor
        else -> ColorScheme.colorScheme.textField.background
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            TextField(
                value = value,
                onValueChange = { newValue ->
                    val filteredValue = when (visualTransformation) {
                        is MaskVisualTransformation -> newValue.filter { it.isDigit() }
                        else -> newValue
                    }

                    if (filteredValue.length <= maxLength) {
                        onValueChange(filteredValue)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                    .onFocusChanged { isFocused = it.isFocused }
                    .focusRequester(focusRequester),
                enabled = enabled,
                readOnly = readOnly,
                textStyle = TextStyle(fontFamily = helloFontFamily()),
                label = {
                    Text(
                        text = label,
                        fontFamily = helloFontFamily(),
                        style = if (isFocused)
                            TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 11.sp,
                                fontFamily = helloFontFamily(),
                                fontWeight = FontWeight(700),
                                color = ColorScheme.colorScheme.defaultColor
                            )
                        else
                            TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 15.4.sp,
                                fontFamily = helloFontFamily(),
                                color = ColorScheme.colorScheme.textField.placeholder
                            )
                    )
                },
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                isError = isError,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                maxLines = maxLines,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = backgroundColor,
                    focusedContainerColor = backgroundColor,
                    disabledContainerColor = backgroundColor,
                    errorContainerColor = backgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    unfocusedTextColor = ColorScheme.colorScheme.textField.text,
                    focusedTextColor = ColorScheme.colorScheme.textField.text,
                    errorTextColor = ColorScheme.colorScheme.textField.text,
                    disabledTextColor = ColorScheme.colorScheme.textField.disabledText,
                    cursorColor = ColorScheme.colorScheme.defaultColor
                )
            )
        }

        if ((isError && error.isNotEmpty()) || maxLength != Int.MAX_VALUE) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isError && error.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = getDrawableIllustration(type = IllustrationType.IC_ALERT),
                            contentDescription = null,
                            tint = ColorScheme.colorScheme.alertColor
                        )

                        Spacer(Modifier.width(8.dp))

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
                } else {
                    Spacer(modifier = Modifier)
                }

                if (maxLength != Int.MAX_VALUE && showMaxLength) {
                    Text(
                        text = "${value.length}/$maxLength",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 15.4.sp,
                            fontFamily = helloFontFamily(),
                            color = ColorScheme.colorScheme.text.secondaryColor
                        )
                    )
                }
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
private fun TextFieldUIPreview() {
    var textValue by remember {
        mutableStateOf("testando")
    }

    HelloTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorScheme.colorScheme.screen.backgroundPrimary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextFieldUI(
                modifier = Modifier
                    .padding(32.dp),
                value = textValue,
                isError = true,
                error = "Nome inválido",
                label = "label",
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {},
                        content = {
                            Icon(
                                painter = painterResource(R.drawable.ic_hide),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    )
                },
                onValueChange = {
                    textValue = it
                }
            )

            TextFieldUI(
                modifier = Modifier
                    .padding(32.dp),
                value = textValue,
                label = "label",
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_email_fill),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {},
                        content = {
                            Icon(
                                painter = painterResource(R.drawable.ic_hide),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    )
                },
                onValueChange = {
                    textValue = it
                }
            )
        }
    }
}
