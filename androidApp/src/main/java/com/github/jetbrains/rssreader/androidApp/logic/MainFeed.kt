package com.github.jetbrains.rssreader.androidApp.logic

import com.github.jetbrains.app.FeedAction
import com.github.jetbrains.app.FeedSideEffect
import com.github.jetbrains.app.FeedState
import com.github.jetbrains.app.FeedStore
import com.github.jetbrains.rssreader.androidApp.ui.base.Presenter

class MainFeed(
    private val store: FeedStore
) : Presenter<FeedState, FeedSideEffect>(store) {

    override fun start() {
        super.start()
        store.dispatch(FeedAction.Refresh(false))
    }

    fun onRefresh() {
        store.dispatch(FeedAction.Refresh(true))
    }
}