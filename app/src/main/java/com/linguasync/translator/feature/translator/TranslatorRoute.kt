package com.linguasync.translator.feature.translator

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.linguasync.translator.R

/**
 * The "stateful" side of the translator screen. It connects [TranslatorViewModel] to the
 * stateless [TranslatorScreen] and handles Android things the screen should not know about:
 * the microphone permission, the clipboard and toasts.
 *
 * There is no visual code here, so there is nothing to preview. Edit [TranslatorScreen] for UI.
 *
 * @param onOpenAbout asks the navigation layer to show the About screen.
 */
@Composable
fun TranslatorRoute(
    onOpenAbout: () -> Unit,
    viewModel: TranslatorViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val copiedMessage = stringResource(R.string.copied)

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.startSession() else viewModel.onPermissionDenied()
    }

    TranslatorScreen(
        state = state,
        actions = TranslatorActions(
            onInputLanguageSelected = viewModel::setInputLanguage,
            onOutputLanguageSelected = viewModel::setOutputLanguage,
            onSwapLanguages = viewModel::swapLanguages,
            onMicClick = {
                val granted = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
                when {
                    state.isSessionActive -> viewModel.stopSession()
                    granted -> viewModel.startSession()
                    else -> permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            },
            onPlayEntry = viewModel::replay,
            onCopyEntry = { entry ->
                clipboard.setText(AnnotatedString(entry.translatedText))
                Toast.makeText(context, copiedMessage, Toast.LENGTH_SHORT).show()
            },
            onClearHistory = viewModel::clearHistory,
            onDismissError = viewModel::dismissError,
            onAboutClick = {
                viewModel.stopSession()
                onOpenAbout()
            },
        ),
    )
}
