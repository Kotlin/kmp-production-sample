package com.github.jetbrains.rssreader.androidApp.logic

import com.github.jetbrains.rssreader.RssReader
import com.github.jetbrains.rssreader.entity.Post
import kotlinx.coroutines.launch

sealed class MainFeedState : State {
    object Empty : MainFeedState()
    data class Progress(val posts: List<Post>) : MainFeedState()
    data class Data(val posts: List<Post>) : MainFeedState()
    data class Error(val error: Throwable, val posts: List<Post>) : MainFeedState()
}

sealed class MainFeedAction : Action {
    data class Refresh(val forceLoad: Boolean) : MainFeedAction()
    data class Data(val posts: List<Post>) : MainFeedAction()
    data class Error(val error: Throwable) : MainFeedAction()
}

sealed class MainFeedEffect : Effect {
    data class Load(val forceLoad: Boolean) : MainFeedEffect()
}

class MainFeed(
    private val rssReader: RssReader
) : Store<MainFeedState, MainFeedAction, MainFeedEffect>(MainFeedState.Empty) {

    override fun start() {
        super.start()
        reduce(MainFeedAction.Refresh(false))
    }

    fun onRefresh() {
        reduce(MainFeedAction.Refresh(true))
    }

    override fun reducer(
        currentState: MainFeedState,
        action: MainFeedAction,
        sideEffects: (effect: MainFeedEffect) -> Unit
    ) = when (currentState) {
        is MainFeedState.Empty -> when (action) {
            is MainFeedAction.Refresh -> {
                sideEffects(MainFeedEffect.Load(action.forceLoad))
                MainFeedState.Progress(emptyList())
            }
            else -> currentState
        }
        is MainFeedState.Data -> when (action) {
            is MainFeedAction.Refresh -> {
                sideEffects(MainFeedEffect.Load(action.forceLoad))
                MainFeedState.Progress(currentState.posts)
            }
            else -> currentState
        }
        is MainFeedState.Error -> when (action) {
            is MainFeedAction.Refresh -> {
                sideEffects(MainFeedEffect.Load(action.forceLoad))
                MainFeedState.Progress(currentState.posts)
            }
            else -> currentState
        }
        is MainFeedState.Progress -> when (action) {
            is MainFeedAction.Data -> {
                MainFeedState.Data(action.posts)
            }
            is MainFeedAction.Error -> {
                MainFeedState.Error(action.error, currentState.posts)
            }
            else -> currentState
        }
    }

    override fun effect(effect: MainFeedEffect) {
        launch {
            try {
                when (effect) {
                    is MainFeedEffect.Load -> {
                        val feed = rssReader.getAllPosts(effect.forceLoad)
                        reduce(MainFeedAction.Data(feed))
                    }
                }
            } catch (e: Throwable) {
                reduce(MainFeedAction.Error(e))
            }
        }
    }
}