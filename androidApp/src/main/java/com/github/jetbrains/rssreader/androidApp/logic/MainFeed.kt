package com.github.jetbrains.rssreader.androidApp.logic

import com.github.jetbrains.rssreader.RssReader
import com.github.jetbrains.rssreader.entity.Feed
import kotlinx.coroutines.launch

sealed class MainFeedState : State {
    object Empty : MainFeedState()
    data class Progress(val feedUrl: String) : MainFeedState()
    data class Data(val feedUrl: String, val feed: Feed) : MainFeedState()
    data class Error(val feedUrl: String, val error: Throwable) : MainFeedState()
}

sealed class MainFeedAction : Action {
    data class Refresh(val feedUrl: String) : MainFeedAction()
    data class Data(val feed: Feed) : MainFeedAction()
    data class Error(val error: Throwable) : MainFeedAction()
}

sealed class MainFeedEffect : Effect {
    data class Load(val feedUrl: String) : MainFeedEffect()
}

class MainFeed(
    private val rssReader: RssReader
) : Store<MainFeedState, MainFeedAction, MainFeedEffect>(MainFeedState.Empty) {
    private val feedUrl = "https://blog.jetbrains.com/kotlin/feed/"

    override fun start() {
        super.start()
        onRefresh()
    }

    fun onRefresh() {
        reduce(MainFeedAction.Refresh(feedUrl))
    }

    override fun reducer(
        currentState: MainFeedState,
        action: MainFeedAction,
        sideEffects: (effect: MainFeedEffect) -> Unit
    ) = when (currentState) {
        is MainFeedState.Empty,
        is MainFeedState.Data,
        is MainFeedState.Error -> when (action) {
            is MainFeedAction.Refresh -> {
                sideEffects(MainFeedEffect.Load(action.feedUrl))
                MainFeedState.Progress(action.feedUrl)
            }
            else -> currentState
        }
        is MainFeedState.Progress -> when (action) {
            is MainFeedAction.Data -> {
                MainFeedState.Data(currentState.feedUrl, action.feed)
            }
            is MainFeedAction.Error -> {
                MainFeedState.Error(currentState.feedUrl, action.error)
            }
            else -> currentState
        }
    }

    override fun effect(effect: MainFeedEffect) {
        launch {
            try {
                when (effect) {
                    is MainFeedEffect.Load -> {
                        val feed = rssReader.getFeed(effect.feedUrl)
                        reduce(MainFeedAction.Data(feed))
                    }
                }
            } catch (e: Throwable) {
                reduce(MainFeedAction.Error(e))
            }
        }
    }
}