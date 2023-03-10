package com.github.jetbrains.rssreader.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.awt.Desktop
import java.net.URI

internal actual val WindowInsets.Companion.navigationBars: WindowInsets
    @Composable
    get() = WindowInsets(0, 0, 0, 0)

internal actual val WindowInsets.Companion.statusBars: WindowInsets
    @Composable
    get() = WindowInsets(0, 0, 0, 0)

internal actual val WindowInsets.Companion.systemBars: WindowInsets
    @Composable
    get() = WindowInsets(0, 0, 0, 0)

@Composable
internal actual fun Modifier.navigationBarsPadding(): Modifier = this

@Composable
internal actual fun Modifier.statusBarsPadding(): Modifier = this

@Composable
internal actual fun Modifier.imePadding(): Modifier = this

@Composable
internal actual fun Dialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) = Dialog(
    onCloseRequest = onDismissRequest,
    focusable = true,
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

internal actual fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    Desktop.getDesktop().browse(uri)
}