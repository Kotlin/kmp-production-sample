package com.github.jetbrains.rssreader.androidApp.ui.util

import com.github.jetbrains.rssreader.androidApp.App


val Int.dp get() = (this * App.INSTANCE.applicationContext.resources.displayMetrics.density).toInt()