import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.compose.App
import com.github.jetbrains.rssreader.core.RssReader
import com.github.jetbrains.rssreader.core.create
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger
import org.koin.dsl.module
import rssreader.composeapp.generated.resources.Res

fun main(args: Array<String>) = application {
    val isDebug = args.contains("--debug")
    val appModule = module {
        single { RssReader.create(isDebug) }
        single { FeedStore(get()) }
    }
    startKoin {
        if (isDebug) logger(PrintLogger(Level.ERROR))
        modules(appModule)
    }

    Window(
        title = stringResource(Res.string.app_name),
        state = rememberWindowState(width = 450.dp, height = 800.dp),
        onCloseRequest = ::exitApplication,
    ) { App() }
}