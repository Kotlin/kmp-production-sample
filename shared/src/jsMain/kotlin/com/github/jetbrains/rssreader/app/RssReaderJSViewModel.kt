package com.github.jetbrains.rssreader.app

import com.github.jetbrains.rssreader.core.entity.Feed
import com.github.jetbrains.rssreader.core.entity.Post
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@ExperimentalJsExport
@JsExport
class FeedStateJsWrapper(
    val progress: Boolean,
    val feeds: Array<FeedJsWrapper>,
    val selectedFeed: Feed? = null
)

@ExperimentalJsExport
@JsExport
class FeedJsWrapper(
    val title: String,
    val link: String,
    val desc: String,
    val imageUrl: String?,
    val posts: Array<Post>,
    val sourceUrl: String,
    val isDefault: Boolean
)

fun FeedState.toJsObject() = FeedStateJsWrapper(
    progress,
    feeds.map { it.toJsObject() }.toTypedArray(),
    selectedFeed
)

fun Feed.toJsObject() = FeedJsWrapper(
    title,
    link,
    desc,
    imageUrl,
    posts.toTypedArray(),
    sourceUrl,
    isDefault
)

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
    fun addFeed(url: String) {
        store.dispatch(FeedAction.Add(url))
    }

    @Suppress("unused")
    fun deleteFeed(url: String) {
        store.dispatch(FeedAction.Delete(url))
    }

    @Suppress("unused")
    fun refreshFeeds(forceLoad: Boolean = true) {
        store.dispatch(FeedAction.Refresh(forceLoad))
    }

    @Suppress("unused")
    fun observeState(callback: (state: FeedStateJsWrapper) -> Unit) {
        mainScope.launch {
            store.observeState().collect {
                callback(it.toJsObject())
            }
        }
    }
}
