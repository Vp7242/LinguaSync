package com.linguasync.translator.feature.about.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.linguasync.translator.R
import com.linguasync.translator.ui.preview.LightDarkPreview
import com.linguasync.translator.ui.preview.PreviewSurface
import com.linguasync.translator.ui.theme.Spacing

/** Credit line for the original open-source project plus a link button. */
@Composable
fun CreditSection(onOpenOriginalProject: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.about_credit),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        TextButton(onClick = onOpenOriginalProject) {
            Icon(Icons.AutoMirrored.Rounded.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(Spacing.Small))
            Text(stringResource(R.string.about_open_source))
        }
    }
}

// ---- Previews ----------------------------------------------------------------------------

@LightDarkPreview
@Composable
private fun CreditSectionPreview() {
    PreviewSurface {
        CreditSection(onOpenOriginalProject = {}, modifier = Modifier.padding(Spacing.Large))
    }
}
