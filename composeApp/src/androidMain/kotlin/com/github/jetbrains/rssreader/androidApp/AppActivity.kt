package com.github.jetbrains.rssreader.androidApp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
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
import kotlinx.coroutines.flow.*
import org.koin.android.ext.android.inject

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ProvideWindowInsets {
                    val store: FeedStore by inject()
                    val scaffoldState = rememberScaffoldState()
                    val error = store.observeSideEffect()
                        .filterIsInstance<FeedSideEffect.Error>()
                        .collectAsState(null)
                    LaunchedEffect(error.value) {
                        error.value?.let {
                            scaffoldState.snackbarHostState.showSnackbar(
                                it.error.message.toString()
                            )
                        }
                    }
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
                        Scaffold(
                            scaffoldState = scaffoldState,
                            snackbarHost = { hostState ->
                                SnackbarHost(
                                    hostState = hostState,
                                    modifier = Modifier.padding(
                                        rememberInsetsPaddingValues(
                                            insets = LocalWindowInsets.current.systemBars,
                                            applyBottom = true
                                        )
                                    )
                                )
                            }
                        ) {
                            Navigator(MainScreen())
                        }
                    }
                }
            }
        }
    }
}
