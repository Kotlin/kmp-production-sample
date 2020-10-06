package com.github.jetbrains.app

import kotlinx.coroutines.flow.Flow

interface State
interface Action
interface Effect

interface Store<S : State, A : Action, E : Effect> {
    fun observeState(): Flow<S>
    fun observeSideEffect(): Flow<E>
    fun dispatch(action: A)
}