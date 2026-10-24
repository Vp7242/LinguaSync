package com.linguasync.translator.feature.translator.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.linguasync.translator.R
import com.linguasync.translator.ui.preview.LightDarkPreview
import com.linguasync.translator.ui.preview.PreviewSurface

/** Top bar with the app name, "clear history" and "about". */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorTopBar(
    hasHistory: Boolean,
    onClearHistory: () -> Unit,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(stringResource(R.string.app_name)) },
        actions = {
            IconButton(onClick = onClearHistory, enabled = hasHistory) {
                Icon(Icons.Rounded.DeleteSweep, contentDescription = stringResource(R.string.clear_history))
            }
            IconButton(onClick = onAboutClick) {
                Icon(Icons.Outlined.Info, contentDescription = stringResource(R.string.about))
            }
        },
    )
}

// ---- Previews ----------------------------------------------------------------------------

@LightDarkPreview
@Composable
private fun TranslatorTopBarPreview() {
    PreviewSurface {
        TranslatorTopBar(hasHistory = true, onClearHistory = {}, onAboutClick = {})
    }
}

@LightDarkPreview
@Composable
private fun TranslatorTopBarEmptyHistoryPreview() {
    PreviewSurface {
        TranslatorTopBar(hasHistory = false, onClearHistory = {}, onAboutClick = {})
    }
}
