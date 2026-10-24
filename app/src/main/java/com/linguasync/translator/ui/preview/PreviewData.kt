package com.linguasync.translator.ui.preview

import com.linguasync.translator.feature.translator.TranslatorUiState
import com.linguasync.translator.model.Languages
import com.linguasync.translator.model.ListeningStatus
import com.linguasync.translator.model.TranslationEntry

/** Sample data shared by every @Preview in the app. */
object PreviewData {

    val entries = listOf(
        TranslationEntry(
            id = 1,
            originalText = "नमस्ते, आप कैसे हैं?",
            translatedText = "Hello, how are you?",
            sourceLanguageName = "Hindi",
            targetLanguageName = "English",
            targetSpeechTag = "en-US",
        ),
        TranslationEntry(
            id = 2,
            originalText = "Where is the nearest train station?",
            translatedText = "¿Dónde está la estación de tren más cercana?",
            sourceLanguageName = "English",
            targetLanguageName = "Spanish",
            targetSpeechTag = "es-ES",
        ),
        TranslationEntry(
            id = 3,
            originalText = "Thank you very much for your help.",
            translatedText = "ご協力ありがとうございました。",
            sourceLanguageName = "English",
            targetLanguageName = "Japanese",
            targetSpeechTag = "ja-JP",
        ),
    )

    val emptyState = TranslatorUiState()

    val historyState = TranslatorUiState(
        inputLanguage = Languages.English,
        outputLanguage = Languages.Spanish,
        history = entries,
    )

    val listeningState = historyState.copy(
        status = ListeningStatus.Listening,
        isSessionActive = true,
        partialText = "Can you recommend a good restaurant",
        micLevel = 0.6f,
    )

    val processingState = historyState.copy(
        status = ListeningStatus.Processing,
        isSessionActive = true,
        recognizedText = "Can you recommend a good restaurant nearby?",
    )

    val downloadingState = processingState.copy(isDownloadingModel = true)

    val speakingState = historyState.copy(
        status = ListeningStatus.Speaking,
        isSessionActive = true,
        recognizedText = "Can you recommend a good restaurant nearby?",
        translatedText = "¿Puede recomendarme un buen restaurante cercano?",
    )

    val errorState = emptyState.copy(
        error = "Could not understand! Please try again.",
    )
}
