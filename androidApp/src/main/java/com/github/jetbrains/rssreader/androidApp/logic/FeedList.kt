package com.github.jetbrains.rssreader.androidApp.logic

import com.github.jetbrains.rssreader.RssReader
import kotlinx.coroutines.launch
import timber.log.Timber

data class FeedListState(val urls: Set<String>) : State

sealed class FeedListAction : Action {
    object Load : FeedListAction()
    data class Add(val url: String) : FeedListAction()
    data class Remove(val url: String) : FeedListAction()
    data class Data(val urls: Set<String>) : FeedListAction()
}

sealed class FeedListEffect : Effect {
    object Load : FeedListEffect()
    data class Add(val url: String) : FeedListEffect()
    data class Remove(val url: String) : FeedListEffect()
    data class Error(val error: Throwable) : FeedListEffect()
}

class FeedList(
    private val rssReader: RssReader
) : Store<FeedListState, FeedListAction, FeedListEffect>(FeedListState(emptySet())) {

    override fun start() {
        super.start()
        reduce(FeedListAction.Load)
    }

    fun addFeed(url: String) {
        reduce(FeedListAction.Add(url))
    }

    fun removeFeed(url: String) {
        reduce(FeedListAction.Remove(url))
    }

    override fun reducer(
        currentState: FeedListState,
        action: FeedListAction,
        sideEffects: (effect: FeedListEffect) -> Unit
    ) = when (action) {
        is FeedListAction.Load -> {
            sideEffects(FeedListEffect.Load)
            currentState
        }
        is FeedListAction.Add -> {
            sideEffects(FeedListEffect.Add(action.url))
            currentState
        }
        is FeedListAction.Remove -> {
            sideEffects(FeedListEffect.Remove(action.url))
            currentState
        }
        is FeedListAction.Data -> {
            FeedListState(action.urls)
        }
    }

    override fun effect(effect: FeedListEffect) {
        launch {
            try {
                when (effect) {
                    is FeedListEffect.Load -> {
                        val urls = rssReader.getAllFeedUrls()
                        reduce(FeedListAction.Data(urls))
                    }
                    is FeedListEffect.Add -> {
                        rssReader.addFeed(effect.url)
                        val urls = rssReader.getAllFeedUrls()
                        reduce(FeedListAction.Data(urls))
                    }
                    is FeedListEffect.Remove -> {
                        rssReader.deleteFeedUrl(effect.url)
                        val urls = rssReader.getAllFeedUrls()
                        reduce(FeedListAction.Data(urls))
                    }
                }
            } catch (e: Throwable) {
                Timber.e(e)
                screenEffect(FeedListEffect.Error(e))
            }
        }
    }
}