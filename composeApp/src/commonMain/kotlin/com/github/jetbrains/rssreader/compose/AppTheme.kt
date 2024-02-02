package com.github.jetbrains.rssreader.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Orange = Color(0xfff8873c)
private val Purple = Color(0xff6b70fc)
private val LightColors = lightColorScheme(
    primary = Orange,
    onPrimary = Color.White,
    primaryContainer = Orange,
    onPrimaryContainer = Color.White,
    secondary = Purple,
    onSecondary = Color.White,
    secondaryContainer = Purple,
    onSecondaryContainer = Color.White
)
private val DarkColors = darkColorScheme(
    primary = Orange,
    onPrimary = Color.White,
    primaryContainer = Orange,
    onPrimaryContainer = Color.White,
    secondary = Purple,
    onSecondary = Color.White,
    secondaryContainer = Purple,
    onSecondaryContainer = Color.White
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = {
            Surface(content = content)
        }
    )
}