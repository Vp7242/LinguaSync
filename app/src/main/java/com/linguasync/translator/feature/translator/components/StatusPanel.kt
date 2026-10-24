package com.linguasync.translator.feature.translator.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.linguasync.translator.R
import com.linguasync.translator.model.ListeningStatus
import com.linguasync.translator.ui.preview.LightDarkPreview
import com.linguasync.translator.ui.preview.PreviewSurface
import com.linguasync.translator.ui.theme.Spacing

/**
 * Shows what the app is doing ("Listening…", "Translating…") together with the live transcript
 * and a progress bar while a language pack is downloading.
 */
@Composable
fun StatusPanel(
    status: ListeningStatus,
    liveText: String,
    isDownloadingModel: Boolean,
    modifier: Modifier = Modifier,
) {
    val isActive = status != ListeningStatus.Idle
    val label = when (status) {
        ListeningStatus.Idle -> R.string.status_idle
        ListeningStatus.Listening -> R.string.status_listening
        ListeningStatus.Processing -> R.string.status_processing
        ListeningStatus.Speaking -> R.string.status_speaking
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        color = if (isActive) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        },
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Spacing.Large, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = stringResource(label),
                style = MaterialTheme.typography.labelLarge,
                color = if (isActive) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
            if (status == ListeningStatus.Listening && liveText.isBlank()) {
                Text(
                    text = stringResource(R.string.status_listening_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                )
            }
            if (liveText.isNotBlank()) {
                Text(
                    text = liveText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (isDownloadingModel) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Text(
                    text = stringResource(R.string.downloading_language_pack),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }
    }
}

// ---- Previews ----------------------------------------------------------------------------

@LightDarkPreview
@Composable
private fun StatusPanelIdlePreview() {
    PreviewSurface {
        StatusPanel(
            ListeningStatus.Idle,
            liveText = "",
            isDownloadingModel = false,
            modifier = Modifier.padding(Spacing.Large),
        )
    }
}

@LightDarkPreview
@Composable
private fun StatusPanelListeningPreview() {
    PreviewSurface {
        StatusPanel(
            ListeningStatus.Listening,
            liveText = "Can you recommend a good restaurant",
            isDownloadingModel = false,
            modifier = Modifier.padding(Spacing.Large),
        )
    }
}

@LightDarkPreview
@Composable
private fun StatusPanelListeningEmptyPreview() {
    PreviewSurface {
        StatusPanel(
            ListeningStatus.Listening,
            liveText = "",
            isDownloadingModel = false,
            modifier = Modifier.padding(Spacing.Large),
        )
    }
}

@LightDarkPreview
@Composable
private fun StatusPanelDownloadingPreview() {
    PreviewSurface {
        StatusPanel(
            ListeningStatus.Processing,
            liveText = "Where is the train station?",
            isDownloadingModel = true,
            modifier = Modifier.padding(Spacing.Large),
        )
    }
}
