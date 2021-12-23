package com.github.jetbrains.rssreader.desktopApp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.core.RssReader
import com.github.jetbrains.rssreader.core.create
import com.github.jetbrains.rssreader.sharedui.AppTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger
import org.koin.dsl.module

private const val isDebug = true

private val appModule = module {
    single { RssReader.create(isDebug) }
    single { FeedStore(get()) }
}

@ExperimentalComposeUiApi
fun main() = application {
    val koinApp = startKoin {
        if (isDebug) logger(PrintLogger(Level.ERROR))
        modules(appModule)
    }

    val backEvents = MutableSharedFlow<Boolean>()
    Window(
        onCloseRequest = ::exitApplication,
        title = "RSS reader",
        state = rememberWindowState(width = 400.dp, height = 800.dp),
        onKeyEvent = { event ->
            if (event.key == Key.Escape && event.type == KeyEventType.KeyUp) {
                GlobalScope.launch { backEvents.emit(true) }
                true
            } else false
        }
    ) {
        AppTheme {
            Navigator(MainScreen()) { navigator ->
                LaunchedEffect(Unit) {
                    val store: FeedStore  = koinApp.koin.get()
                    store.dispatch(FeedAction.Refresh(false))
                    backEvents.onEach { navigator.pop() }.launchIn(this)
                }
                CurrentScreen()
            }
        }
    }
}