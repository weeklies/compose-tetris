package com.jetgame.tetris.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.jetgame.tetris.logic.defaultFontFamily

private val LightColorPalette =
    lightColors(
        primary = light_Naut,
        onPrimary = light_onNaut,
        secondary = light_Orange,
        onSecondary = light_onOrange,
        error = md_theme_light_error,
        onError = md_theme_light_onError,
        background = md_theme_light_background,
        onBackground = md_theme_light_onBackground,
        surface = light_NautContainer,
        onSurface = light_onNautContainer,
    )

private val DarkColorPalette =
    darkColors(
        primary = dark_Naut,
        onPrimary = dark_onNaut,
        secondary = dark_Orange,
        onSecondary = dark_onOrange,
        error = md_theme_dark_error,
        onError = md_theme_dark_onError,
        background = md_theme_dark_background,
        onBackground = md_theme_dark_onBackground,
        surface = dark_NautContainer,
        onSurface = dark_onNautContainer
    )

@Composable
fun ComposetetrisTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography(defaultFontFamily = defaultFontFamily),
        shapes = Shapes,
        content = content
    )
}
