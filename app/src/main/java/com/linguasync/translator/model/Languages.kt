package com.linguasync.translator.model

/**
 * Languages supported by the app.
 *
 * TO ADD A LANGUAGE:
 *  1. Add a `val` below. `code` must be an ML Kit code (see `TranslateLanguage`),
 *     `speechTag` a BCP-47 tag such as "hi-IN".
 *  2. Add it to the [all] list. The pickers and the About screen update automatically.
 *
 * The first 15 entries come from the original Python project's language table
 * (Punjabi was replaced by Marathi because ML Kit's translator does not offer Punjabi).
 */
object Languages {

    /** Input-only pseudo language: listen in the phone's language, then detect what was said. */
    val Auto = Language(name = "Auto-detect", nativeName = "", code = "", speechTag = null)

    val English = Language("English", "English", "en", "en-US")
    val Hindi = Language("Hindi", "हिन्दी", "hi", "hi-IN")
    val Bengali = Language("Bengali", "বাংলা", "bn", "bn-IN")
    val Spanish = Language("Spanish", "Español", "es", "es-ES")
    val Chinese = Language("Chinese (Simplified)", "中文", "zh", "zh-CN")
    val Russian = Language("Russian", "Русский", "ru", "ru-RU")
    val Japanese = Language("Japanese", "日本語", "ja", "ja-JP")
    val Korean = Language("Korean", "한국어", "ko", "ko-KR")
    val German = Language("German", "Deutsch", "de", "de-DE")
    val French = Language("French", "Français", "fr", "fr-FR")
    val Tamil = Language("Tamil", "தமிழ்", "ta", "ta-IN")
    val Telugu = Language("Telugu", "తెలుగు", "te", "te-IN")
    val Kannada = Language("Kannada", "ಕನ್ನಡ", "kn", "kn-IN")
    val Gujarati = Language("Gujarati", "ગુજરાતી", "gu", "gu-IN")
    val Marathi = Language("Marathi", "मराठी", "mr", "mr-IN")
    val Urdu = Language("Urdu", "اردو", "ur", "ur-PK")
    val Arabic = Language("Arabic", "العربية", "ar", "ar-SA")
    val Portuguese = Language("Portuguese", "Português", "pt", "pt-BR")
    val Italian = Language("Italian", "Italiano", "it", "it-IT")

    /** Languages that can be picked as output (and as an explicit input). */
    val all: List<Language> = listOf(
        English, Hindi, Bengali, Spanish, Chinese, Russian, Japanese, Korean, German, French,
        Tamil, Telugu, Kannada, Gujarati, Marathi, Urdu, Arabic, Portuguese, Italian,
    )

    /** Languages that can be picked as input: Auto first, then everything else. */
    val inputOptions: List<Language> = listOf(Auto) + all

    /** Languages that can be picked as output. */
    val outputOptions: List<Language> = all
}
