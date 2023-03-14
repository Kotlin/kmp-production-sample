package com.github.jetbrains.rssreader.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.jetbrains.rssreader.App

internal actual val WindowInsets.Companion.navigationBars: WindowInsets
    @Composable
    get() = WindowInsets.navigationBars


internal actual val WindowInsets.Companion.statusBars: WindowInsets
    @Composable
    get() = WindowInsets.statusBars


internal actual val WindowInsets.Companion.systemBars: WindowInsets
    @Composable
    get() = WindowInsets.systemBars

@Composable
internal actual fun Modifier.navigationBarsPadding(): Modifier = navigationBarsPadding()

@Composable
internal actual fun Modifier.statusBarsPadding(): Modifier = statusBarsPadding()

@Composable
internal actual fun Modifier.imePadding(): Modifier = imePadding()

internal actual fun openUrl(url: String?) {
    val uri = url?.let { Uri.parse(it) } ?: return
    App.context.startActivity(Intent(Intent.ACTION_VIEW, uri))
}