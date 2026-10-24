package com.linguasync.translator.feature.about.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.linguasync.translator.model.Language
import com.linguasync.translator.model.Languages
import com.linguasync.translator.ui.preview.LightDarkPreview
import com.linguasync.translator.ui.preview.PreviewSurface
import com.linguasync.translator.ui.theme.Spacing

/** Wrapping row of pill-shaped chips, one per language. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LanguageChips(languages: List<Language>, modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Small, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(Spacing.Small),
    ) {
        languages.forEach { language -> LanguageChip(language.name) }
    }
}

@Composable
fun LanguageChip(name: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = Spacing.Medium, vertical = 6.dp),
        )
    }
}

// ---- Previews ----------------------------------------------------------------------------

@LightDarkPreview
@Composable
private fun LanguageChipPreview() {
    PreviewSurface {
        LanguageChip("Hindi", modifier = Modifier.padding(Spacing.Large))
    }
}

@LightDarkPreview
@Composable
private fun LanguageChipsPreview() {
    PreviewSurface {
        LanguageChips(Languages.all, modifier = Modifier.padding(Spacing.Large))
    }
}
