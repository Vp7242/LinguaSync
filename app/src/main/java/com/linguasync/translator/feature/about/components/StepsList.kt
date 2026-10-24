package com.linguasync.translator.feature.about.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.linguasync.translator.ui.preview.LightDarkPreview
import com.linguasync.translator.ui.preview.PreviewSurface
import com.linguasync.translator.ui.theme.Spacing

/** Numbered list of short steps. */
@Composable
fun StepsList(steps: List<String>, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        steps.forEachIndexed { index, step ->
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = (index + 1).toString(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Spacer(Modifier.width(Spacing.Medium))
                Text(step, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

// ---- Previews ----------------------------------------------------------------------------

@LightDarkPreview
@Composable
private fun StepsListPreview() {
    PreviewSurface {
        StepsList(
            steps = listOf("Speak", "Translate", "Listen"),
            modifier = Modifier.padding(Spacing.Large),
        )
    }
}
