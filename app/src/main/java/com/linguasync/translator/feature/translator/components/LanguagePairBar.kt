package com.linguasync.translator.feature.translator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.linguasync.translator.R
import com.linguasync.translator.model.Language
import com.linguasync.translator.model.Languages
import com.linguasync.translator.ui.preview.LightDarkPreview
import com.linguasync.translator.ui.preview.PreviewSurface
import com.linguasync.translator.ui.theme.Spacing

/** A tappable card showing the current language with a small label above it. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageButton(
    label: String,
    language: Language,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(onClick = onClick, modifier = modifier) {
        Column(
            modifier = Modifier.padding(
                start = Spacing.Large,
                top = 10.dp,
                bottom = 10.dp,
                end = Spacing.Small,
            ),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = language.displayName(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Icon(Icons.Rounded.ArrowDropDown, contentDescription = null)
            }
        }
    }
}

/** "From [language]  ⇄  To [language]" bar at the top of the translator screen. */
@Composable
fun LanguagePairBar(
    inputLanguage: Language,
    outputLanguage: Language,
    onInputClick: () -> Unit,
    onOutputClick: () -> Unit,
    onSwapClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall),
    ) {
        LanguageButton(
            label = stringResource(R.string.from_label),
            language = inputLanguage,
            onClick = onInputClick,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onSwapClick, enabled = !inputLanguage.isAuto) {
            Icon(Icons.Rounded.SwapHoriz, contentDescription = stringResource(R.string.swap_languages))
        }
        LanguageButton(
            label = stringResource(R.string.to_label),
            language = outputLanguage,
            onClick = onOutputClick,
            modifier = Modifier.weight(1f),
        )
    }
}

// ---- Previews ----------------------------------------------------------------------------

@LightDarkPreview
@Composable
private fun LanguageButtonPreview() {
    PreviewSurface {
        LanguageButton(
            label = "From",
            language = Languages.Hindi,
            onClick = {},
            modifier = Modifier.padding(Spacing.Large),
        )
    }
}

@LightDarkPreview
@Composable
private fun LanguagePairBarPreview() {
    PreviewSurface {
        LanguagePairBar(
            inputLanguage = Languages.English,
            outputLanguage = Languages.Spanish,
            onInputClick = {},
            onOutputClick = {},
            onSwapClick = {},
            modifier = Modifier.padding(Spacing.Large),
        )
    }
}

@LightDarkPreview
@Composable
private fun LanguagePairBarAutoPreview() {
    PreviewSurface {
        LanguagePairBar(
            inputLanguage = Languages.Auto,
            outputLanguage = Languages.English,
            onInputClick = {},
            onOutputClick = {},
            onSwapClick = {},
            modifier = Modifier.padding(Spacing.Large),
        )
    }
}
