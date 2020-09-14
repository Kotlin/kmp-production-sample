package com.github.jetbrains.rssreader.androidApp.logic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import timber.log.Timber

interface State
interface Action
interface Effect

interface Screen<T : State> {
    fun render(state: T)
}

abstract class Store<S : State, A : Action, E : Effect>(
    initState: S
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val storeName = javaClass.simpleName
    private var isStarted = false

    private var state: S = initState
        set(value) {
            if (field != value) {
                Timber.d("$storeName new state($value)")
                field = value
                screen?.render(value)
            }
        }

    private var screen: Screen<S>? = null

    protected open fun start() {
        Timber.d("$storeName start($state)")
        isStarted = true
    }

    fun attach(screen: Screen<S>) {
        if (!isStarted) start()
        Timber.d("$storeName attach")
        if (this.screen != screen) {
            this.screen = screen
            screen.render(state)
        }
    }

    fun detach() {
        Timber.d("$storeName detach")
        this.screen = null
    }

    open fun destroy() {
        Timber.d("$storeName destroy")
        cancel()
        isStarted = false
    }

    protected fun reduce(action: A) {
        Timber.d("$storeName action($action)")
        state = reducer(state, action, this::effect)
    }

    protected abstract fun reducer(currentState: S, action: A, sideEffects: (effect: E) -> Unit): S

    protected open fun effect(effect: E) {
        Timber.d("$storeName effect($effect)")
    }
}