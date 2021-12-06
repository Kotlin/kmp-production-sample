package com.github.jetbrains.rssreader.sharedui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Orange = Color(0xfff8873c)
private val Purple = Color(0xff6b70fc)
private val LightColors = lightColors(
    primary = Orange,
    primaryVariant = Orange,
    onPrimary = Color.White,
    secondary = Purple,
    onSecondary = Color.White
)
private val DarkColors = darkColors(
    primary = Orange,
    primaryVariant = Orange,
    onPrimary = Color.White,
    secondary = Purple,
    onSecondary = Color.White
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = {
            Surface(content = content)
        }
    )
}