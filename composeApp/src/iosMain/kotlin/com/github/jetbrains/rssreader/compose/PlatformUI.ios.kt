package com.github.jetbrains.rssreader.compose

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

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
) {}

internal actual fun openUrl(url: String?) {
    val nsUrl = url?.let { NSURL.URLWithString(it) } ?: return
    UIApplication.sharedApplication.openURL(nsUrl)
}