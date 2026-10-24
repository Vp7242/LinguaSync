package com.linguasync.translator.feature.translator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.linguasync.translator.model.TranslationEntry
import com.linguasync.translator.ui.preview.LightDarkPreview
import com.linguasync.translator.ui.preview.PreviewData
import com.linguasync.translator.ui.preview.PreviewSurface
import com.linguasync.translator.ui.theme.Spacing

/** Newest translation at the bottom, right above the microphone. */
@Composable
fun ConversationList(
    entries: List<TranslationEntry>,
    onPlay: (TranslationEntry) -> Unit,
    onCopy: (TranslationEntry) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val newestFirst = remember(entries) { entries.asReversed() }

    LaunchedEffect(entries.size) {
        if (entries.isNotEmpty()) listState.animateScrollToItem(0)
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        reverseLayout = true,
        contentPadding = PaddingValues(Spacing.Large),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
    ) {
        items(newestFirst, key = { it.id }) { entry ->
            TranslationCard(
                entry = entry,
                onPlay = { onPlay(entry) },
                onCopy = { onCopy(entry) },
            )
        }
    }
}

// ---- Previews ----------------------------------------------------------------------------

@LightDarkPreview
@Composable
private fun ConversationListPreview() {
    PreviewSurface {
        ConversationList(
            entries = PreviewData.entries,
            onPlay = {},
            onCopy = {},
            modifier = Modifier.height(520.dp),
        )
    }
}
