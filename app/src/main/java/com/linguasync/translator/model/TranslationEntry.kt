package com.linguasync.translator.model

/** One finished translation shown in the conversation list. */
data class TranslationEntry(
    val id: Long,
    val originalText: String,
    val translatedText: String,
    val sourceLanguageName: String,
    val targetLanguageName: String,
    val targetSpeechTag: String?,
)
