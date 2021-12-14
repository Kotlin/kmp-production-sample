package com.github.jetbrains.rssreader.androidApp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedSideEffect
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.sharedui.AppTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class AppActivity : ComponentActivity(), CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val store: FeedStore by inject()
    private var effectJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ProvideWindowInsets {
                    Box(
                        Modifier.padding(
                            rememberInsetsPaddingValues(
                                insets = LocalWindowInsets.current.systemBars,
                                applyStart = true,
                                applyTop = false,
                                applyEnd = true,
                                applyBottom = false
                            )
                        )
                    ) {
                        Navigator(MainScreen())
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        effectJob = store.observeSideEffect().onEach { effect ->
            if (effect is FeedSideEffect.Error) {
                Toast.makeText(this, effect.error.message, Toast.LENGTH_SHORT).show()
            }
        }.launchIn(this)
    }

    override fun onPause() {
        super.onPause()
        effectJob?.cancel()
    }

    override fun onDestroy() {
        if (isFinishing) cancel()
        super.onDestroy()
    }
}
