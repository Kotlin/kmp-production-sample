package com.github.jetbrains.rssreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cafe.adriel.voyager.navigator.Navigator
import com.github.jetbrains.rssreader.app.FeedSideEffect
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.composeui.AppTheme
import com.github.jetbrains.rssreader.composeui.MainScreen
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.android.ext.android.inject

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            AppTheme {
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
                Box(modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize())
                {
                    Scaffold(
                        scaffoldState = scaffoldState,
                        snackbarHost = { hostState ->
                            SnackbarHost(
                                hostState = hostState,
                                modifier = Modifier.Companion.padding(
                                    WindowInsets.Companion.systemBars
                                        .only(WindowInsetsSides.Companion.Bottom)
                                        .asPaddingValues()
                                )
                            )
                        }
                    ) { contentPadding ->
                        Column(modifier = Modifier.padding(contentPadding)) {
                            Navigator(MainScreen())
                        }
                    }
                }
            }
        }
    }
}