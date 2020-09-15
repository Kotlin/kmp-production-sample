package com.github.jetbrains.rssreader.androidApp.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {
    protected lateinit var runId: String
        private set
    private var instanceStateSaved: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.getString(FIRST_RUN_ID)?.let { restoredId ->
            runId = restoredId
        } ?: run {
            runId = "${javaClass.simpleName}[${hashCode()}]"
            onFirstCreate()
        }
    }

    protected open fun onFirstCreate() {}

    override fun onStart() {
        super.onStart()
        instanceStateSaved = false
    }

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(FIRST_RUN_ID, runId)
        instanceStateSaved = true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRealDestroy()) onFinalDestroy()
    }

    protected open fun onFinalDestroy() {}

    // This is android, baby!
    private fun isRealRemoving(): Boolean =
        (isRemoving && !instanceStateSaved) || // Because isRemoving == true for fragment in backstack on screen rotation
                ((parentFragment as? BaseFragment)?.isRealRemoving() ?: false)

    // It will be valid only for 'onDestroy()' method
    private fun isRealDestroy(): Boolean =
        when {
            activity?.isChangingConfigurations == true -> false
            activity?.isFinishing == true -> true
            else -> isRealRemoving()
        }

    open fun onBackPressed() {}

    companion object {
        private const val FIRST_RUN_ID = "state_first_run_id"
    }
}