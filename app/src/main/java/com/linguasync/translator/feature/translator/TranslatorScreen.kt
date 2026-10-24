package com.linguasync.translator.feature.translator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.linguasync.translator.R
import com.linguasync.translator.feature.translator.components.ConversationList
import com.linguasync.translator.feature.translator.components.EmptyState
import com.linguasync.translator.feature.translator.components.ErrorBanner
import com.linguasync.translator.feature.translator.components.LanguagePairBar
import com.linguasync.translator.feature.translator.components.LanguagePickerDialog
import com.linguasync.translator.feature.translator.components.MicButton
import com.linguasync.translator.feature.translator.components.StatusPanel
import com.linguasync.translator.feature.translator.components.TranslatorTopBar
import com.linguasync.translator.model.Languages
import com.linguasync.translator.model.ListeningStatus
import com.linguasync.translator.ui.preview.PreviewData
import com.linguasync.translator.ui.preview.PreviewSurface
import com.linguasync.translator.ui.preview.ScreenPreview
import com.linguasync.translator.ui.theme.Spacing

private enum class PickerTarget { Input, Output }

/**
 * The main screen. It is stateless: everything it shows comes from [state] and every user
 * action is reported through [actions], which keeps it easy to preview and test.
 *
 * Layout, top to bottom:
 *  1. [TranslatorTopBar]
 *  2. [LanguagePairBar]
 *  3. [ErrorBanner] (only when there is an error)
 *  4. [ConversationList] or [EmptyState]
 *  5. [StatusPanel]
 *  6. [MicButton]
 */
@Composable
fun TranslatorScreen(
    state: TranslatorUiState,
    actions: TranslatorActions,
    modifier: Modifier = Modifier,
) {
    var pickerTarget by rememberSaveable { mutableStateOf<PickerTarget?>(null) }

    val liveText = when (state.status) {
        ListeningStatus.Listening -> state.partialText
        ListeningStatus.Processing, ListeningStatus.Speaking -> state.recognizedText
        ListeningStatus.Idle -> ""
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TranslatorTopBar(
                hasHistory = state.history.isNotEmpty(),
                onClearHistory = actions.onClearHistory,
                onAboutClick = actions.onAboutClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            LanguagePairBar(
                inputLanguage = state.inputLanguage,
                outputLanguage = state.outputLanguage,
                onInputClick = { pickerTarget = PickerTarget.Input },
                onOutputClick = { pickerTarget = PickerTarget.Output },
                onSwapClick = actions.onSwapLanguages,
                modifier = Modifier.padding(horizontal = Spacing.Large, vertical = Spacing.Small),
            )

            AnimatedVisibility(visible = state.error != null) {
                ErrorBanner(
                    message = state.error.orEmpty(),
                    onDismiss = actions.onDismissError,
                    modifier = Modifier.padding(horizontal = Spacing.Large, vertical = Spacing.ExtraSmall),
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                if (state.history.isEmpty()) {
                    EmptyState(modifier = Modifier.align(Alignment.Center))
                } else {
                    ConversationList(
                        entries = state.history,
                        onPlay = actions.onPlayEntry,
                        onCopy = actions.onCopyEntry,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }

            StatusPanel(
                status = state.status,
                liveText = liveText,
                isDownloadingModel = state.isDownloadingModel,
                modifier = Modifier.padding(horizontal = Spacing.Large),
            )

            MicButton(
                isActive = state.isSessionActive,
                status = state.status,
                level = state.micLevel,
                onClick = actions.onMicClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = Spacing.Small),
            )
        }
    }

    when (pickerTarget) {
        PickerTarget.Input -> LanguagePickerDialog(
            title = stringResource(R.string.select_input_language),
            languages = Languages.inputOptions,
            selected = state.inputLanguage,
            onSelect = {
                actions.onInputLanguageSelected(it)
                pickerTarget = null
            },
            onDismiss = { pickerTarget = null },
        )
        PickerTarget.Output -> LanguagePickerDialog(
            title = stringResource(R.string.select_output_language),
            languages = Languages.outputOptions,
            selected = state.outputLanguage,
            onSelect = {
                actions.onOutputLanguageSelected(it)
                pickerTarget = null
            },
            onDismiss = { pickerTarget = null },
        )
        null -> Unit
    }
}

// ---- Previews ----------------------------------------------------------------------------
// One preview per state. To check a new state, add a sample to PreviewData and a line here.

@Composable
private fun PreviewTranslatorScreen(state: TranslatorUiState) {
    PreviewSurface {
        TranslatorScreen(state = state, actions = TranslatorActions())
    }
}

@ScreenPreview
@Composable
private fun TranslatorScreenEmptyPreview() = PreviewTranslatorScreen(PreviewData.emptyState)

@ScreenPreview
@Composable
private fun TranslatorScreenHistoryPreview() = PreviewTranslatorScreen(PreviewData.historyState)

@ScreenPreview
@Composable
private fun TranslatorScreenListeningPreview() = PreviewTranslatorScreen(PreviewData.listeningState)

@ScreenPreview
@Composable
private fun TranslatorScreenProcessingPreview() = PreviewTranslatorScreen(PreviewData.processingState)

@ScreenPreview
@Composable
private fun TranslatorScreenDownloadingPreview() = PreviewTranslatorScreen(PreviewData.downloadingState)

@ScreenPreview
@Composable
private fun TranslatorScreenSpeakingPreview() = PreviewTranslatorScreen(PreviewData.speakingState)

@ScreenPreview
@Composable
private fun TranslatorScreenErrorPreview() = PreviewTranslatorScreen(PreviewData.errorState)
