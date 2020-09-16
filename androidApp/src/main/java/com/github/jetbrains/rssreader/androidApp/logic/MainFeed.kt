package com.github.jetbrains.rssreader.androidApp.logic

import com.github.jetbrains.rssreader.RssReader
import com.github.jetbrains.rssreader.entity.Feed
import kotlinx.coroutines.launch

sealed class MainFeedState : State {
    object Empty : MainFeedState()
    data class Progress(val feed: Feed?) : MainFeedState()
    data class Data(val feed: Feed) : MainFeedState()
    data class Error(val error: Throwable, val feed: Feed?) : MainFeedState()
}

sealed class MainFeedAction : Action {
    data class Refresh(val feedUrl: String, val forceLoad: Boolean) : MainFeedAction()
    data class Data(val feed: Feed) : MainFeedAction()
    data class Error(val error: Throwable) : MainFeedAction()
}

sealed class MainFeedEffect : Effect {
    data class Load(val feedUrl: String, val forceLoad: Boolean) : MainFeedEffect()
}

class MainFeed(
    private val rssReader: RssReader,
    private val feedUrl: String
) : Store<MainFeedState, MainFeedAction, MainFeedEffect>(MainFeedState.Empty) {

    override fun start() {
        super.start()
        reduce(MainFeedAction.Refresh(feedUrl, false))
    }

    fun onRefresh() {
        reduce(MainFeedAction.Refresh(feedUrl, true))
    }

    override fun reducer(
        currentState: MainFeedState,
        action: MainFeedAction,
        sideEffects: (effect: MainFeedEffect) -> Unit
    ) = when (currentState) {
        is MainFeedState.Empty -> when (action) {
            is MainFeedAction.Refresh -> {
                sideEffects(MainFeedEffect.Load(action.feedUrl, action.forceLoad))
                MainFeedState.Progress(null)
            }
            else -> currentState
        }
        is MainFeedState.Data -> when (action) {
            is MainFeedAction.Refresh -> {
                sideEffects(MainFeedEffect.Load(action.feedUrl, action.forceLoad))
                MainFeedState.Progress(currentState.feed)
            }
            else -> currentState
        }
        is MainFeedState.Error -> when (action) {
            is MainFeedAction.Refresh -> {
                sideEffects(MainFeedEffect.Load(action.feedUrl, action.forceLoad))
                MainFeedState.Progress(currentState.feed)
            }
            else -> currentState
        }
        is MainFeedState.Progress -> when (action) {
            is MainFeedAction.Data -> {
                MainFeedState.Data(action.feed)
            }
            is MainFeedAction.Error -> {
                MainFeedState.Error(action.error, currentState.feed)
            }
            else -> currentState
        }
    }

    override fun effect(effect: MainFeedEffect) {
        launch {
            try {
                when (effect) {
                    is MainFeedEffect.Load -> {
                        val feed = rssReader.getFeed(effect.feedUrl, effect.forceLoad)
                        reduce(MainFeedAction.Data(feed))
                    }
                }
            } catch (e: Throwable) {
                reduce(MainFeedAction.Error(e))
            }
        }
    }
}