package com.github.jetbrains.rssreader.composeapp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.core.RssReader
import com.github.jetbrains.rssreader.core.create
import com.seiko.imageloader.ImageLoaderBuilder

actual fun buildImageLoader() = ImageLoaderBuilder(AndroidApp.INSTANCE).build()
actual val AppStore: FeedStore by lazy {
    FeedStore(RssReader.create(AndroidApp.INSTANCE, true))
}

class AndroidApp : Application() {
    override fun onCreate() {
        INSTANCE = this
        super.onCreate()
    }

    companion object {
        lateinit var INSTANCE: AndroidApp
            private set
    }
}

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}