package com.linguasync.translator.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.linguasync.translator.ui.theme.Spacing

/** Shows the main theme colours side by side, handy when editing Color.kt / Theme.kt. */
@LightDarkPreview
@Composable
private fun ThemePalettePreview() {
    PreviewSurface {
        Column(
            modifier = Modifier.padding(Spacing.Large),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small),
        ) {
            Text("LinguaSync palette", style = MaterialTheme.typography.titleMedium)
            val swatches = listOf(
                "primary" to MaterialTheme.colorScheme.primary,
                "primaryContainer" to MaterialTheme.colorScheme.primaryContainer,
                "secondary" to MaterialTheme.colorScheme.secondary,
                "secondaryContainer" to MaterialTheme.colorScheme.secondaryContainer,
                "error" to MaterialTheme.colorScheme.error,
                "errorContainer" to MaterialTheme.colorScheme.errorContainer,
            )
            swatches.forEach { (name, color) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
                ) {
                    Box(
                        Modifier
                            .size(28.dp)
                            .background(color, RoundedCornerShape(6.dp))
                    )
                    Text(name, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
