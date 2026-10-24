package com.linguasync.translator.ui.preview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.linguasync.translator.ui.theme.LinguaSyncTheme

/** Wraps a preview in the app theme (fixed colours, no dynamic colour) on a background. */
@Composable
fun PreviewSurface(content: @Composable () -> Unit) {
    LinguaSyncTheme(dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background,
            content = content,
        )
    }
}
