package com.github.jetbrains.rssreader.compose

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@get:Composable
internal expect val WindowInsets.Companion.navigationBars: WindowInsets

@get:Composable
internal expect val WindowInsets.Companion.statusBars: WindowInsets

@get:Composable
internal expect val WindowInsets.Companion.systemBars: WindowInsets

@Composable
internal expect fun Modifier.navigationBarsPadding(): Modifier

@Composable
internal expect fun Modifier.statusBarsPadding(): Modifier

@Composable
internal expect fun Modifier.imePadding(): Modifier

internal expect fun openUrl(url: String?)