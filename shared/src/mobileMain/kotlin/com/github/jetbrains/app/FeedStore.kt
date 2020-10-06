package com.github.jetbrains.app

import com.github.aakira.napier.Napier
import com.github.jetbrains.rssreader.RssReader
import com.github.jetbrains.rssreader.entity.Feed
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

data class FeedState(
    val progress: Boolean,
    val feeds: List<Feed>
) : State

sealed class FeedAction : Action {
    data class Refresh(val forceLoad: Boolean) : FeedAction()
    data class Add(val url: String) : FeedAction()
    data class Delete(val url: String) : FeedAction()
    data class Data(val feeds: List<Feed>) : FeedAction()
    data class Error(val error: Exception) : FeedAction()
}

sealed class FeedSideEffect : Effect {
    object Empty : FeedSideEffect()
    data class Load(val forceLoad: Boolean) : FeedSideEffect()
    data class Add(val url: String) : FeedSideEffect()
    data class Delete(val url: String) : FeedSideEffect()
    data class Error(val error: Exception) : FeedSideEffect()
}

@OptIn(ExperimentalCoroutinesApi::class)
class FeedStore : Store<FeedState, FeedAction, FeedSideEffect> {
    private val state = MutableStateFlow(FeedState(false, emptyList()))
    private val sideEffect = MutableStateFlow<FeedSideEffect>(FeedSideEffect.Empty)

    override fun observeState(): Flow<FeedState> = state
    override fun observeSideEffect(): Flow<FeedSideEffect> = sideEffect

    override fun dispatch(action: FeedAction) {
        Napier.d("Action: $action")
        val oldState = state.value
        val newState = if (oldState.progress) when (action) {
            is FeedAction.Refresh,
            is FeedAction.Add,
            is FeedAction.Delete -> {
                sideEffect.value = FeedSideEffect.Error(Exception("In progress"))
                oldState
            }
            is FeedAction.Data -> {
                FeedState(false, action.feeds)
            }
            is FeedAction.Error -> {
                sideEffect.value = FeedSideEffect.Error(action.error)
                FeedState(false, oldState.feeds)
            }
        } else when (action) {
            is FeedAction.Refresh -> {
                sideEffect.value = FeedSideEffect.Load(action.forceLoad)
                FeedState(true, oldState.feeds)
            }
            is FeedAction.Add -> {
                sideEffect.value = FeedSideEffect.Add(action.url)
                FeedState(true, oldState.feeds)
            }
            is FeedAction.Delete -> {
                sideEffect.value = FeedSideEffect.Delete(action.url)
                FeedState(true, oldState.feeds)
            }
            is FeedAction.Data,
            is FeedAction.Error -> {
                sideEffect.value = FeedSideEffect.Error(Exception("Unexpected action"))
                oldState
            }
        }
        if (newState != oldState) {
            Napier.d("NewState: $newState")
            state.value = newState
        }
    }
}

class FeedEngine(
    private val rssReader: RssReader,
    private val store: FeedStore
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {
    fun start() {
        launch {
            store.observeSideEffect().collect { effect ->
                try {
                    Napier.d("Effect: $effect")
                    when (effect) {
                        is FeedSideEffect.Load -> {
                            val allFeeds = rssReader.getAllFeeds(effect.forceLoad)
                            store.dispatch(FeedAction.Data(allFeeds))
                        }
                        is FeedSideEffect.Add -> {
                            rssReader.addFeed(effect.url)
                            val allFeeds = rssReader.getAllFeeds(false)
                            store.dispatch(FeedAction.Data(allFeeds))
                        }
                        is FeedSideEffect.Delete -> {
                            rssReader.deleteFeed(effect.url)
                            val allFeeds = rssReader.getAllFeeds(false)
                            store.dispatch(FeedAction.Data(allFeeds))
                        }
                    }
                } catch (e: Exception) {
                    store.dispatch(FeedAction.Error(e))
                }
            }
        }
    }

    fun stop() {
        cancel()
    }
}