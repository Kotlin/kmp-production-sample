package com.github.jetbrains.rssreader.composeapp

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

@Composable
actual fun RemoteImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier,
    alignment: Alignment,
    contentScale: ContentScale
) {
    val painter = rememberAsyncImagePainter(imageUrl)
    Image(painter, contentDescription, modifier, alignment, contentScale)
}