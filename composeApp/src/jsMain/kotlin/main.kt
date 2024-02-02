import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.compose.App
import com.github.jetbrains.rssreader.core.RssReader
import com.github.jetbrains.rssreader.core.create
import org.jetbrains.skiko.wasm.onWasmReady
import org.koin.core.context.startKoin
import org.koin.dsl.module

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val appModule = module {
        single { RssReader.create(true) }
        single { FeedStore(get()) }
    }
    startKoin {
        modules(appModule)
    }
    onWasmReady {
        CanvasBasedWindow("RSS Reader") {
            App()
        }
    }
}
