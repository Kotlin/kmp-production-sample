package com.github.jetbrains.rssreader.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.github.jetbrains.rssreader.app.FeedSideEffect
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.images.MRImages
import com.github.jetbrains.rssreader.strings.MRStrings
import io.github.skeptick.libres.compose.painterResource
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.mp.KoinPlatformTools

@Composable
internal fun App() = AppTheme {
    val store: FeedStore = KoinPlatformTools.defaultContext().get().get()
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
    CommonDialogHandleableApplication {
        Box(
            Modifier.padding(
                WindowInsets.systemBars
                    .only(WindowInsetsSides.Start + WindowInsetsSides.End)
                    .asPaddingValues()
            )
        ) {
            Navigator(MainScreen()) { navigator ->
                Scaffold(
                    scaffoldState = scaffoldState,
                    snackbarHost = { hostState ->
                        SnackbarHost(
                            hostState = hostState,
                            modifier = Modifier.padding(
                                WindowInsets.systemBars
                                    .only(WindowInsetsSides.Bottom)
                                    .asPaddingValues()
                            )
                        )
                    },
                    topBar = {
                        TopAppBar(
                            navigationIcon = {
                                Image(
                                    painter = painterResource(
                                        if (navigator.canPop) MRImages.ic_arrow_back else MRImages.ic_home
                                    ),
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clip(CircleShape)
                                        .clickable { navigator.pop() }
                                        .padding(8.dp),
                                    contentDescription = null
                                )
                            },
                            title = {
                                Text(MRStrings.app_name)
                            },
                            modifier = Modifier
                                .background(MaterialTheme.colors.primary)
                                .padding(WindowInsets.statusBars.asPaddingValues())
                        )
                    }
                ) {
                    CurrentScreen()
                }
            }
        }
    }
}