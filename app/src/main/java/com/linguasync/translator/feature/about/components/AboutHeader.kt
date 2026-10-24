package com.linguasync.translator.feature.about.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.linguasync.translator.R
import com.linguasync.translator.ui.preview.LightDarkPreview
import com.linguasync.translator.ui.preview.PreviewSurface
import com.linguasync.translator.ui.theme.Spacing

/** Logo, app name, version and the one-paragraph description. */
@Composable
fun AboutHeader(versionName: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.app_logo),
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(24.dp)),
        )
        Spacer(Modifier.height(Spacing.Medium))
        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineMedium)
        Text(
            text = stringResource(R.string.about_version, versionName),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(Spacing.Large))
        Text(
            text = stringResource(R.string.about_description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

// ---- Previews ----------------------------------------------------------------------------

@LightDarkPreview
@Composable
private fun AboutHeaderPreview() {
    PreviewSurface {
        AboutHeader(versionName = "1.0.0", modifier = Modifier.padding(Spacing.Large))
    }
}
