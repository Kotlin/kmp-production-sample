package com.github.jetbrains.rssreader.sharedui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal expect fun AsyncImage(
    url: String,
    modifier: Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
)

@Composable
internal expect fun LocalImage(
    resName: String,
    modifier: Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
)

@Composable
internal expect fun Dialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
)

@Composable
internal expect fun _str(resName: String): String

internal expect fun Modifier.systemStatusBarsHeight(additional: Dp = 0.dp): Modifier
internal expect fun Modifier.systemNavigationBarsHeight(additional: Dp = 0.dp): Modifier
internal expect fun Modifier.systemNavigationBarsWithImePadding(): Modifier
