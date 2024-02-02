import androidx.compose.ui.window.ComposeUIViewController
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.compose.App
import com.github.jetbrains.rssreader.core.RssReader
import com.github.jetbrains.rssreader.core.create
import org.koin.core.context.startKoin
import org.koin.dsl.module
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    val appModule = module {
        single { RssReader.create(true) }
        single { FeedStore(get()) }
    }
    startKoin {
        modules(appModule)
    }

    return ComposeUIViewController { App() }
}
