package com.github.jetbrains.rssreader.androidApp.ui.util

import com.github.jetbrains.rssreader.core.entity.Feed
import java.util.*

fun Feed.shortName(): String =
    title
        .replace(" ", "")
        .take(2)
        .toUpperCase(Locale.getDefault())