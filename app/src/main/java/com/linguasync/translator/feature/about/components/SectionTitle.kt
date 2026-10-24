package com.linguasync.translator.feature.about.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.linguasync.translator.ui.preview.LightDarkPreview
import com.linguasync.translator.ui.preview.PreviewSurface
import com.linguasync.translator.ui.theme.Spacing

/** Heading above a block of the About screen. */
@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = Spacing.Small),
    )
}

// ---- Previews ----------------------------------------------------------------------------

@LightDarkPreview
@Composable
private fun SectionTitlePreview() {
    PreviewSurface {
        SectionTitle("How it works", modifier = Modifier.padding(Spacing.Large))
    }
}
