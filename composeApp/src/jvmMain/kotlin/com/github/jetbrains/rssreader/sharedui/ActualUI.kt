package com.github.jetbrains.rssreader.sharedui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.jetbrains.skia.Image

internal actual fun Modifier.systemStatusBarsHeight(additional: Dp): Modifier = this

internal actual fun Modifier.systemNavigationBarsHeight(additional: Dp): Modifier = this

internal actual fun Modifier.systemNavigationBarsWithImePadding(): Modifier = this

@Composable
internal actual fun _str(resName: String) = resName

private val httpClient = HttpClient(OkHttp)
private suspend fun loadImageBitmap(url: String): Result<ImageBitmap> {
    return try {
        val image = httpClient.get(url).readBytes()
        Result.success(Image.makeFromEncoded(image).toComposeImageBitmap())
    } catch (e: Exception) {
        Result.failure(e)
    }
}

@Composable
internal actual fun AsyncImage(
    url: String,
    modifier: Modifier,
    alignment: Alignment,
    contentScale: ContentScale,
    alpha: Float,
    colorFilter: ColorFilter?
) {
    var isLoading by remember { mutableStateOf(false) }
    var hasFail by remember { mutableStateOf(false) }
    var imageBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    LaunchedEffect(url) {
        isLoading = true
        loadImageBitmap(url)
            .onSuccess {
                imageBitmap = it
            }
            .onFailure {
                hasFail = true
            }
        isLoading = false
    }

    when {
        isLoading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        hasFail -> {
            //OnFail()
        }
        else -> {
            imageBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    modifier = modifier,
                    contentDescription = null,
                    alignment = alignment,
                    contentScale = contentScale,
                    alpha = alpha,
                    colorFilter = colorFilter
                )
            } //?: OnFail()
        }
    }
}

@Composable
internal actual fun LocalImage(
    resName: String,
    modifier: Modifier,
    alignment: Alignment,
    contentScale: ContentScale,
    alpha: Float,
    colorFilter: ColorFilter?
) {
    Image(
        modifier = modifier,
        painter = painterResource(resName),
        contentDescription = null,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter
    )
}

@Composable
internal actual fun Dialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onCloseRequest = onDismissRequest,
        focusable = true,
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}