package com.linguasync.translator.feature.translator.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.linguasync.translator.R
import com.linguasync.translator.ui.preview.LightDarkPreview
import com.linguasync.translator.ui.preview.PreviewSurface
import com.linguasync.translator.ui.theme.Spacing

/** Dismissable error message. */
@Composable
fun ErrorBanner(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
    ) {
        Row(
            modifier = Modifier.padding(
                start = Spacing.Large,
                top = Spacing.ExtraSmall,
                bottom = Spacing.ExtraSmall,
                end = Spacing.ExtraSmall,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Rounded.ErrorOutline, contentDescription = null, modifier = Modifier.size(20.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Spacing.Medium, vertical = Spacing.Small),
            )
            IconButton(onClick = onDismiss) {
                Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.dismiss))
            }
        }
    }
}

// ---- Previews ----------------------------------------------------------------------------

@LightDarkPreview
@Composable
private fun ErrorBannerPreview() {
    PreviewSurface {
        ErrorBanner(
            message = "Could not understand! Please try again.",
            onDismiss = {},
            modifier = Modifier.padding(Spacing.Large),
        )
    }
}

@LightDarkPreview
@Composable
private fun ErrorBannerLongPreview() {
    PreviewSurface {
        ErrorBanner(
            message = "Translation failed. Check your internet connection (needed once to download a language pack).",
            onDismiss = {},
            modifier = Modifier.padding(Spacing.Large),
        )
    }
}
