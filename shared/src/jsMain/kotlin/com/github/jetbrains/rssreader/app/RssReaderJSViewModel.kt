package com.github.jetbrains.rssreader.app

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@ExperimentalJsExport
@JsExport
object RssReaderJsViewModel : KoinComponent {
    private val mainScope = MainScope()
    private var store : FeedStore

    init {
        initKoin()
        store = get()
    }

    @Suppress("unused")
    fun cancel() {
        mainScope.cancel()
    }

    @Suppress("unused")
    fun refreshFeeds() {
        store.dispatch(FeedAction.Refresh(true))
    }

    @Suppress("unused")
    fun observeStore(callback: (state: FeedState) -> Unit) {
        mainScope.launch {
            store.observeState().collect {
                callback(it)
            }
        }
    }
}
