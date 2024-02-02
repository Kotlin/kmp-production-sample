package com.github.jetbrains.rssreader.compose

import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.component.setupDefaultComponents
import okio.Path
import okio.Path.Companion.toPath
import java.awt.Desktop
import java.net.URI

internal actual fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    Desktop.getDesktop().browse(uri)
}

internal actual fun ComponentRegistryBuilder.setupDefaultComponents() = setupDefaultComponents()

internal actual fun getImageCacheDirectoryPath(): Path = "media/".toPath()