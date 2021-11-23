package com.github.jetbrains.rssreader.app

import com.github.jetbrains.rssreader.core.createRssReader
import org.koin.dsl.module
import org.koin.core.context.startKoin

fun initKoin() {
    val deps = module {
        single { createRssReader(false) }
        single { FeedStore(get()) }
    }

    startKoin {
        modules(deps)
    }
}
