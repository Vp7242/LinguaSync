package com.linguasync.translator.feature.translator.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.linguasync.translator.R
import com.linguasync.translator.model.Language
import com.linguasync.translator.model.Languages
import com.linguasync.translator.ui.preview.LightDarkPreview
import com.linguasync.translator.ui.preview.PreviewSurface

/** Scrollable list of languages with a check mark on the selected one. */
@Composable
fun LanguagePickerContent(
    languages: List<Language>,
    selected: Language,
    onSelect: (Language) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.heightIn(max = 420.dp),
        contentPadding = PaddingValues(vertical = 4.dp),
    ) {
        items(languages, key = { it.code }) { language ->
            ListItem(
                headlineContent = { Text(language.displayName()) },
                supportingContent = { Text(language.displaySubtitle()) },
                trailingContent = {
                    if (language == selected) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = stringResource(R.string.selected),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier.clickable { onSelect(language) },
            )
        }
    }
}

/** Dialog wrapper around [LanguagePickerContent]. */
@Composable
fun LanguagePickerDialog(
    title: String,
    languages: List<Language>,
    selected: Language,
    onSelect: (Language) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { LanguagePickerContent(languages, selected, onSelect) },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        },
    )
}

// ---- Previews ----------------------------------------------------------------------------

@LightDarkPreview
@Composable
private fun LanguagePickerContentPreview() {
    PreviewSurface {
        LanguagePickerContent(
            languages = Languages.inputOptions,
            selected = Languages.Auto,
            onSelect = {},
        )
    }
}
