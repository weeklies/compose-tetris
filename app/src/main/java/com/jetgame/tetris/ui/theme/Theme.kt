package com.jetgame.tetris.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jetgame.tetris.logic.defaultFontFamily

private val LightColorPalette =
    lightColors(
        primary = light_Purple,
        onPrimary = light_onPurple,
        secondary = light_Orange,
        onSecondary = light_onOrange,
        error = md_theme_light_error,
        onError = md_theme_light_onError,
        background = md_theme_light_background,
        onBackground = md_theme_light_onBackground,
        surface = light_PurpleContainer,
        onSurface = light_onPurpleContainer,
    )

private val DarkColorPalette =
    darkColors(
        primary = dark_Purple,
        onPrimary = dark_onPurple,
        secondary = dark_Orange,
        onSecondary = dark_onOrange,
        error = md_theme_dark_error,
        onError = md_theme_dark_onError,
        background = Color(0xCC1a1c2c),
        onBackground = md_theme_dark_onBackground,
        surface = dark_PurpleContainer,
        onSurface = dark_onPurpleContainer
    )

@Composable
fun TetrominautsTheme(darkTheme: Boolean = true, content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography(defaultFontFamily),
        shapes = Shapes,
        content = content
    )
}
