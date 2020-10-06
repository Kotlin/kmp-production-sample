package com.github.jetbrains.rssreader.androidApp.logic

import com.github.jetbrains.app.FeedAction
import com.github.jetbrains.app.FeedSideEffect
import com.github.jetbrains.app.FeedState
import com.github.jetbrains.app.FeedStore
import com.github.jetbrains.rssreader.androidApp.ui.base.Presenter

class FeedList(
    private val store: FeedStore
) : Presenter<FeedState, FeedSideEffect>(store) {

    fun addFeed(url: String) {
        store.dispatch(FeedAction.Add(url))
    }

    fun removeFeed(url: String) {
        store.dispatch(FeedAction.Delete(url))
    }
}