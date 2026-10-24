package com.linguasync.translator.feature.translator

import com.linguasync.translator.model.Language
import com.linguasync.translator.model.Languages
import com.linguasync.translator.model.ListeningStatus
import com.linguasync.translator.model.TranslationEntry

/** Everything the UI needs to draw the translator screen. */
data class TranslatorUiState(
    val inputLanguage: Language = Languages.Auto,
    val outputLanguage: Language = Languages.English,
    val status: ListeningStatus = ListeningStatus.Idle,
    /** True while a session is running (keeps listening after every translation). */
    val isSessionActive: Boolean = false,
    /** Live partial transcript while the user is speaking. */
    val partialText: String = "",
    /** Last full recognized sentence. */
    val recognizedText: String = "",
    /** Last translation. */
    val translatedText: String = "",
    /** Normalised microphone level, 0f..1f. */
    val micLevel: Float = 0f,
    val isDownloadingModel: Boolean = false,
    val error: String? = null,
    val history: List<TranslationEntry> = emptyList(),
)
