package com.github.jetbrains.rssreader.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.moriatsushi.insetsx.ExperimentalSoftwareKeyboardApi
import com.moriatsushi.insetsx.imePadding

/**
 * Main idea was found here: https://github.com/LucasAlfare/FullscreenComposable
 */

internal enum class FullScreenState { Active, Inactive }

internal class MutableFullScreenState(
    var state: MutableState<FullScreenState> = mutableStateOf(FullScreenState.Inactive)
)

internal class FullScreenComposableRef(
    var composableReference: @Composable () -> Unit = {}
)

internal val LocalMutableFullScreenState = compositionLocalOf { MutableFullScreenState() }

internal val LocalFullScreenComposableReference = compositionLocalOf { FullScreenComposableRef() }

@Composable
internal fun CommonDialogHandleableApplication(
    applicationContent: @Composable () -> Unit
) {
    val mutableFullScreenState = MutableFullScreenState()
    val fullScreenComposableReference = FullScreenComposableRef()

    CompositionLocalProvider(
        LocalMutableFullScreenState provides mutableFullScreenState,
        LocalFullScreenComposableReference provides fullScreenComposableReference
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // always draws the main application content
            applicationContent()

            // decides if we must draw the full screen content
            if (mutableFullScreenState.state.value == FullScreenState.Active) {
                fullScreenComposableReference.composableReference()
            }
        }
    }
}

internal interface DialogRunner<T> {
    fun show(data: T)
}

@OptIn(ExperimentalSoftwareKeyboardApi::class)
@Composable
internal fun <T> CommonDialog(
    content: @Composable (data: T, close: () -> Unit) -> Unit
): DialogRunner<T> {
    val outerBoxBackground = Color.Gray.copy(alpha = 0.5f)
    var dialogData: T? by remember { mutableStateOf(null) }

    val globalDialogView = LocalFullScreenComposableReference.current
    val globalToggleState = LocalMutableFullScreenState.current
    var localToggleState by remember { mutableStateOf(FullScreenState.Inactive) }

    if (localToggleState == FullScreenState.Active) {
        globalDialogView.composableReference = {
            Box(
                modifier = Modifier
                    .imePadding()
                    .background(outerBoxBackground)
                    .fillMaxSize()
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        dialogData = null
                        globalToggleState.state.value = FullScreenState.Inactive
                    }
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) { /* pass */ }
                ) {
                    dialogData?.let {
                        content(it) {
                            dialogData = null
                            globalToggleState.state.value = FullScreenState.Inactive
                        }
                    }
                }
            }
        }
        LocalMutableFullScreenState.current.state.value = localToggleState
        localToggleState = FullScreenState.Inactive
    }

    return object : DialogRunner<T> {
        override fun show(data: T) {
            dialogData = data
            localToggleState = FullScreenState.Active
        }
    }
}