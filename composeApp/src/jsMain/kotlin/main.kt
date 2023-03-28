import androidx.compose.ui.window.Window
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.compose.App
import com.github.jetbrains.rssreader.core.RssReader
import com.github.jetbrains.rssreader.core.create
import com.github.jetbrains.rssreader.strings.MRStrings
import org.jetbrains.skiko.wasm.onWasmReady
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger
import org.koin.dsl.module

fun main()  {
    val isDebug = true
    val appModule = module {
        single { RssReader.create(isDebug) }
        single { FeedStore(get()) }
    }
    startKoin {
        if (isDebug) logger(PrintLogger(Level.ERROR))
        modules(appModule)
    }

    onWasmReady {
        Window(MRStrings.app_name) {
            App()
        }
    }
}