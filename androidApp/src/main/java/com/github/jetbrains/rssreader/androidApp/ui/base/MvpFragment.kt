package com.github.jetbrains.rssreader.androidApp.ui.base

import androidx.annotation.LayoutRes
import com.github.jetbrains.app.Effect
import com.github.jetbrains.app.State
import com.github.jetbrains.app.Store
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber

interface View<S : State, E : Effect> {
    fun render(state: S)
    fun effect(effect: E) {}
}

abstract class Presenter<S : State, E : Effect>(
    private val store: Store<S, *, E>
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val presenterName = javaClass.simpleName
    private var isStarted = false

    private var view: View<S, E>? = null
    private var stateJob: Job? = null
    private var effectJob: Job? = null

    protected open fun start() {
        Timber.d("$presenterName start")
        isStarted = true
    }

    fun attach(view: View<S, E>) {
        if (this.view != view) {
            Timber.d("$presenterName attach")
            this.view = view
            stateJob = launch { store.observeState().collect { view.render(it) } }
            effectJob = launch { store.observeSideEffect().collect { view.effect(it) } }
        }
        if (!isStarted) start()
    }

    fun detach() {
        Timber.d("$presenterName detach")
        this.view = null
        stateJob?.cancel()
        effectJob?.cancel()
    }

    open fun destroy() {
        Timber.d("$presenterName destroy")
        cancel()
        isStarted = false
    }
}

abstract class MvpFragment<S : State, E : Effect>(
    @LayoutRes contentLayoutId: Int
) : BaseFragment(contentLayoutId), View<S, E> {
    private val viewName = javaClass.simpleName
    abstract val presenter: Presenter<S, E>

    override fun onStart() {
        super.onStart()
        presenter.attach(this)
    }

    override fun render(state: S) {
        Timber.d("$viewName Render: $state")
    }

    override fun effect(effect: E) {
        Timber.d("$viewName Effect: $effect")
    }

    override fun onStop() {
        super.onStop()
        presenter.detach()
    }

    override fun onFinalDestroy() {
        super.onFinalDestroy()
        presenter.destroy()
    }
}