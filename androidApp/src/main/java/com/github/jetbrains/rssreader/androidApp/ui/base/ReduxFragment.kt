package com.github.jetbrains.rssreader.androidApp.ui.base

import androidx.annotation.LayoutRes
import com.github.jetbrains.rssreader.androidApp.logic.Screen
import com.github.jetbrains.rssreader.androidApp.logic.State
import com.github.jetbrains.rssreader.androidApp.logic.Store

abstract class ReduxFragment<T : State>(
    @LayoutRes contentLayoutId: Int
) : BaseFragment(contentLayoutId), Screen<T> {
    abstract val store: Store<T, *, *>

    override fun onStart() {
        super.onStart()
        store.attach(this)
    }

    override fun onStop() {
        super.onStop()
        store.detach()
    }

    override fun onFinalDestroy() {
        super.onFinalDestroy()
        store.destroy()
    }
}