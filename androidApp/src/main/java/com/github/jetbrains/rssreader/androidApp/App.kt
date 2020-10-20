package com.github.jetbrains.rssreader.androidApp

import android.app.Application
import com.github.jetbrains.app.FeedEngine
import com.github.jetbrains.app.FeedStore
import com.github.jetbrains.rssreader.RssReader
import com.github.jetbrains.rssreader.androidApp.logic.FeedList
import com.github.jetbrains.rssreader.androidApp.logic.MainFeed
import com.github.jetbrains.rssreader.androidApp.ui.feedlist.FeedListFragment
import com.github.jetbrains.rssreader.androidApp.ui.mainfeed.MainFeedFragment
import com.github.jetbrains.rssreader.create
import com.github.terrakok.cicerone.Cicerone
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
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private val appModule = module {
        single { RssReader.create(get(), BuildConfig.DEBUG) }
        single { FeedStore() }
        single { FeedEngine(get(), get()) }

        val cicerone = Cicerone.create()
        single { cicerone.router }
        single { cicerone.getNavigatorHolder() }

        scope<MainFeedFragment> {
            scoped { MainFeed(get(), get()) }
        }

        scope<FeedListFragment> {
            scoped { FeedList(get(), get()) }
        }
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.ERROR)

            androidContext(this@App)
            modules(appModule)
        }
    }

    companion object {
        internal lateinit var INSTANCE: App
            private set
    }
}