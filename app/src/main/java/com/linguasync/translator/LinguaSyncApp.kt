package com.linguasync.translator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.linguasync.translator.navigation.AppNavigation
import com.linguasync.translator.ui.theme.LinguaSyncTheme

/** Root of the UI: theme + background + navigation. */
@Composable
fun LinguaSyncApp() {
    LinguaSyncTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            AppNavigation()
        }
    }
}
