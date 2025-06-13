package com.github.jetbrains.rssreader.composeui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.jetbrains.rssreader.R
import com.github.jetbrains.rssreader.core.entity.Feed

@Composable
fun AddFeedDialog(
    onAdd: (String) -> Unit,
    onDismiss: () -> Unit
) = Dialog(
    onDismissRequest = onDismiss
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        val input = remember { mutableStateOf(TextFieldValue()) }
        Text(text = stringResource(R.string.rss_feed_url))
        TextField(
            maxLines = 3,
            value = input.value,
            onValueChange = { input.value = it }
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            modifier = Modifier.align(Alignment.End),
            onClick = {
                onAdd(
                    input.value.text.replace("http://", "https://")
                )
            }
        ) {
            Text(text = stringResource(R.string.add))
        }
    }
}

@Composable
fun DeleteFeedDialog(
    feed: Feed,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) = Dialog(
    onDismissRequest = onDismiss
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = feed.sourceUrl)
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            modifier = Modifier.align(Alignment.End),
            onClick = { onDelete() }
        ) {
            Text(text = stringResource(R.string.remove))
        }
    }
}