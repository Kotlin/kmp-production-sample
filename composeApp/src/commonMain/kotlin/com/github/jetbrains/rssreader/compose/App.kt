package com.github.jetbrains.rssreader.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedSideEffect
import com.github.jetbrains.rssreader.app.FeedStore
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import kotlinx.coroutines.flow.filterIsInstance
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.mp.KoinPlatformTools
import rssreader.composeapp.generated.resources.Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun App() = AppTheme {
    val store: FeedStore = KoinPlatformTools.defaultContext().get().get()
    val snackbarHostState = SnackbarHostState()
    val error = store.observeSideEffect()
        .filterIsInstance<FeedSideEffect.Error>()
        .collectAsState(null)
    LaunchedEffect(error.value) {
        error.value?.let {
            snackbarHostState.showSnackbar(
                it.error.message.toString()
            )
        }
    }

    CompositionLocalProvider(
        LocalImageLoader provides generateImageLoader(),
    ) {
        Navigator(MainScreen()) { navigator ->
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
                    )
                },
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            Image(
                                painter = painterResource(
                                    if (navigator.canPop) {
                                        Res.drawable.ic_arrow_back
                                    } else {
                                        Res.drawable.ic_refresh
                                    }
                                ),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        if (navigator.canPop) {
                                            navigator.pop()
                                        } else {
                                            store.dispatch(FeedAction.Refresh(true))
                                        }
                                    }
                                    .padding(8.dp),
                                contentDescription = null
                            )
                        },
                        title = {
                            Text(stringResource(Res.string.app_name))
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            ) {
                Box(
                    Modifier
                        .consumeWindowInsets(WindowInsets.safeDrawing)
                        .padding(it)
                ) {
                    CurrentScreen()
                }
            }
        }
    }
}

private fun generateImageLoader(
    memCacheSize: Int = 32 * 1024 * 1024,
    diskCacheSize: Int = 512 * 1024 * 1024
) = ImageLoader {
    interceptor {
        memoryCacheConfig {
            maxSizeBytes(memCacheSize)
        }
        diskCacheConfig {
            directory(getImageCacheDirectoryPath())
            maxSizeBytes(diskCacheSize.toLong())
        }
    }
    components {
        setupDefaultComponents()
    }
}