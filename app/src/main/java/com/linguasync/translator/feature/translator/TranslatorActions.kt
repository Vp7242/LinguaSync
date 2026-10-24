package com.linguasync.translator.feature.translator

import com.linguasync.translator.model.Language
import com.linguasync.translator.model.TranslationEntry

/**
 * Everything the user can do on the translator screen.
 *
 * Grouping the callbacks keeps [TranslatorScreen]'s signature short: to add a new action, add
 * one line here, wire it in [TranslatorRoute], and use it in the screen. Every callback defaults
 * to "do nothing", so previews can just write `TranslatorActions()`.
 */
data class TranslatorActions(
    val onInputLanguageSelected: (Language) -> Unit = {},
    val onOutputLanguageSelected: (Language) -> Unit = {},
    val onSwapLanguages: () -> Unit = {},
    val onMicClick: () -> Unit = {},
    val onPlayEntry: (TranslationEntry) -> Unit = {},
    val onCopyEntry: (TranslationEntry) -> Unit = {},
    val onClearHistory: () -> Unit = {},
    val onDismissError: () -> Unit = {},
    val onAboutClick: () -> Unit = {},
)
