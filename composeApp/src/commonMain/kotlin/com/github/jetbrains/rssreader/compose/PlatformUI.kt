package com.github.jetbrains.rssreader.compose

import com.seiko.imageloader.component.ComponentRegistryBuilder
import okio.Path

internal expect fun openUrl(url: String?)
internal expect fun ComponentRegistryBuilder.setupDefaultComponents()
internal expect fun getImageCacheDirectoryPath(): Path