package com.github.jetbrains.rssreader.compose

import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.component.setupDefaultComponents
import kotlinx.browser.window
import okio.FileSystem
import okio.Path

internal actual fun openUrl(url: String?) {
    url?.let { window.open(it) }
}

internal actual fun ComponentRegistryBuilder.setupDefaultComponents() = setupDefaultComponents()

internal actual fun getImageCacheDirectoryPath(): Path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY