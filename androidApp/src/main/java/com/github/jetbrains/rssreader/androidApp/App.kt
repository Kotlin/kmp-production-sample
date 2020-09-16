package com.github.jetbrains.rssreader.androidApp

import android.app.Application
import com.github.jetbrains.rssreader.RssReader
import com.github.jetbrains.rssreader.androidApp.logic.MainFeed
import com.github.jetbrains.rssreader.androidApp.ui.mainfeed.MainFeedFragment
import com.github.jetbrains.rssreader.create
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
        initKoin()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private val appModule = module {
        single {
            RssReader.create(get(), BuildConfig.DEBUG)
        }

        scope<MainFeedFragment> {
            scoped { MainFeed(get()) }
        }
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.ERROR)

            androidContext(this@App)
            modules(appModule)
        }
    }
}