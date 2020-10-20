package com.github.jetbrains.rssreader.androidApp.logic

import com.github.jetbrains.app.FeedAction
import com.github.jetbrains.app.FeedSideEffect
import com.github.jetbrains.app.FeedState
import com.github.jetbrains.app.FeedStore
import com.github.jetbrains.rssreader.androidApp.Screens
import com.github.jetbrains.rssreader.androidApp.ui.base.Presenter
import com.github.jetbrains.rssreader.entity.Feed
import com.github.terrakok.cicerone.Router

class MainFeed(
    private val store: FeedStore,
    private val router: Router
) : Presenter<FeedState, FeedSideEffect>(store) {

    override fun start() {
        super.start()
        store.dispatch(FeedAction.Refresh(false))
    }

    fun onRefresh() {
        store.dispatch(FeedAction.Refresh(true))
    }

    fun onSelectFeed(feed: Feed?) {
        store.dispatch(FeedAction.SelectFeed(feed))
    }

    fun onEditFeedList() {
        router.navigateTo(Screens.FeedList())
    }

    fun onBackPressed() {
        router.exit()
    }
}