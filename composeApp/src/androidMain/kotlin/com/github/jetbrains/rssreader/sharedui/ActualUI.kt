package com.github.jetbrains.rssreader.sharedui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import coil.compose.rememberImagePainter
import com.github.jetbrains.rssreader.androidApp.R
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsHeight

@Composable
internal actual fun AsyncImage(
    url: String,
    modifier: Modifier,
    alignment: Alignment,
    contentScale: ContentScale,
    alpha: Float,
    colorFilter: ColorFilter?
) {
    Image(
        painter = rememberImagePainter(url),
        modifier = modifier,
        contentDescription = null,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter
    )
}

@Composable
internal actual fun LocalImage(
    resName: String,
    modifier: Modifier,
    alignment: Alignment,
    contentScale: ContentScale,
    alpha: Float,
    colorFilter: ColorFilter?
) {
    Image(
        imageVector = ImageVector.vectorResource(resNameToId(resName)),
        modifier = modifier,
        contentDescription = null,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter
    )
}

private fun resNameToId(resName: String): Int = when (resName) {
    "ic_edit.xml" -> R.drawable.ic_edit
    "ic_add.xml" -> R.drawable.ic_add
    else -> error("Unknown resName: $resName")
}

@Composable
internal actual fun _str(resName: String): String = when (resName) {
    "Add" -> stringResource(R.string.add)
    "All" -> stringResource(R.string.all)
    "Remove" -> stringResource(R.string.remove)
    "Rss feed url" -> stringResource(R.string.rss_feed_url)
    else -> error("Unknown resName: $resName")
}

internal actual fun Modifier.systemStatusBarsHeight(additional: Dp): Modifier =
    statusBarsHeight(additional)

internal actual fun Modifier.systemNavigationBarsHeight(additional: Dp): Modifier =
    navigationBarsHeight(additional)

internal actual fun Modifier.systemNavigationBarsWithImePadding(): Modifier =
    navigationBarsWithImePadding()

@Composable
internal actual fun Dialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) = androidx.compose.ui.window.Dialog(onDismissRequest = onDismissRequest, content = content)