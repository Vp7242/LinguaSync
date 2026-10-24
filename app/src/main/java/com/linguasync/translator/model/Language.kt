package com.linguasync.translator.model

import java.util.Locale

/**
 * A language the user can speak or hear.
 *
 * @param name       English display name.
 * @param nativeName Name written in the language itself.
 * @param code       ML Kit translation code (e.g. "hi"). Blank for [Languages.Auto].
 * @param speechTag  BCP-47 tag used for speech recognition and text-to-speech (e.g. "hi-IN").
 */
data class Language(
    val name: String,
    val nativeName: String,
    val code: String,
    val speechTag: String?,
) {
    val isAuto: Boolean get() = code.isBlank()

    val locale: Locale? get() = speechTag?.let { Locale.forLanguageTag(it) }
}
