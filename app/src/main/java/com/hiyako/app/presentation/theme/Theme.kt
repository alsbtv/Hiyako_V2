package com.hiyako.app.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val BaseColorScheme = darkColorScheme(
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun HiyakoTheme(
    themeColor: String = "Red",
    content: @Composable () -> Unit
) {
    val primaryColor = when (themeColor) {
        "Green" -> GreenPrimary
        "Purple" -> PurplePrimary
        "Blue" -> BluePrimary
        else -> RedPrimary
    }

    val colorScheme = BaseColorScheme.copy(primary = primaryColor)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
