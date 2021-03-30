package com.github.jetbrains.rssreader.androidApp

import android.app.Application
import com.github.jetbrains.app.FeedStore
import com.github.jetbrains.rssreader.RssReader
import com.github.jetbrains.rssreader.androidApp.sync.RefreshWorker
import com.github.jetbrains.rssreader.create
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.android.AppReducer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        initLogger()
        initKoin()
        launchBackgroundSync()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private val appModule = module {
        single { RssReader.create(get(), BuildConfig.DEBUG) }
        single { FeedStore(get()) }
        single { Modo(AppReducer(this@App)) }
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.ERROR)

            androidContext(this@App)
            modules(appModule)
        }
    }

    private fun launchBackgroundSync() {
        RefreshWorker.enqueue(this)
    }

    companion object {
        internal lateinit var INSTANCE: App
            private set
    }
}