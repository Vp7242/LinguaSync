package com.linguasync.translator.feature.translator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.linguasync.translator.R
import com.linguasync.translator.model.TranslationEntry
import com.linguasync.translator.ui.preview.LightDarkPreview
import com.linguasync.translator.ui.preview.PreviewData
import com.linguasync.translator.ui.preview.PreviewSurface
import com.linguasync.translator.ui.theme.Spacing

/** One recognized sentence together with its translation. */
@Composable
fun TranslationCard(
    entry: TranslationEntry,
    onPlay: () -> Unit,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            modifier = Modifier.padding(
                start = Spacing.Large,
                top = 14.dp,
                end = Spacing.Small,
                bottom = Spacing.ExtraSmall,
            ),
        ) {
            Text(
                text = stringResource(
                    R.string.arrow_between_languages,
                    entry.sourceLanguageName,
                    entry.targetLanguageName,
                ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = entry.originalText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = Spacing.Small),
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                text = entry.translatedText,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(end = Spacing.Small),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(onClick = onCopy) {
                    Icon(Icons.Rounded.ContentCopy, contentDescription = stringResource(R.string.copy_translation))
                }
                IconButton(onClick = onPlay) {
                    Icon(Icons.AutoMirrored.Rounded.VolumeUp, contentDescription = stringResource(R.string.play_translation))
                }
            }
        }
    }
}

// ---- Previews ----------------------------------------------------------------------------

@LightDarkPreview
@Composable
private fun TranslationCardPreview() {
    PreviewSurface {
        TranslationCard(
            entry = PreviewData.entries[1],
            onPlay = {},
            onCopy = {},
            modifier = Modifier.padding(Spacing.Large),
        )
    }
}
