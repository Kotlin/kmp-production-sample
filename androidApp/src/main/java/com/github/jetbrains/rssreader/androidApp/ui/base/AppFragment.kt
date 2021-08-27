package com.github.jetbrains.rssreader.androidApp.ui.base

import androidx.annotation.LayoutRes
import com.github.jetbrains.rssreader.app.Effect
import com.github.jetbrains.rssreader.app.State
import com.github.jetbrains.rssreader.app.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class AppFragment<S : State, E : Effect>(
    @LayoutRes contentLayoutId: Int
) : BaseFragment(contentLayoutId),
    CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val fragmentName by lazy { "${javaClass.simpleName}[${hashCode()}]" }

    private var stateJob: Job? = null
    private var effectJob: Job? = null
    abstract val store: Store<S, *, E>

    override fun onStart() {
        super.onStart()
        stateJob = launch { store.observeState().collect { render(it) } }
        effectJob = launch { store.observeSideEffect().collect { effect(it) } }
    }

    protected open fun render(state: S) {
        Timber.d("$fragmentName Render: $state")
    }

    protected open fun effect(effect: E) {
        Timber.d("$fragmentName Effect: $effect")
    }

    override fun onStop() {
        stateJob?.cancel()
        effectJob?.cancel()
        super.onStop()
    }
}