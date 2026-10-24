package com.linguasync.translator.data.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

/**
 * Thin wrapper around Android's [SpeechRecognizer] (replaces `SpeechRecognition` + `PyAudio`
 * from the Python version). All methods must be called on the main thread.
 */
class SpeechRecognizerManager(
    private val context: Context,
    private val callbacks: Callbacks,
) {

    enum class Error { NoSpeech, NoMatch, Busy, Network, Permission, Audio, Other }

    interface Callbacks {
        fun onRms(level: Float)
        fun onPartial(text: String)
        fun onFinal(text: String)
        fun onError(error: Error)
    }

    private var recognizer: SpeechRecognizer? = null

    val isAvailable: Boolean
        get() = SpeechRecognizer.isRecognitionAvailable(context)

    /** Starts listening. A null [languageTag] means "use the phone's default language". */
    fun start(languageTag: String?) {
        val engine = recognizer ?: SpeechRecognizer.createSpeechRecognizer(context).also {
            it.setRecognitionListener(listener)
            recognizer = it
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
            )
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            if (languageTag != null) {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageTag)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languageTag)
            }
        }
        engine.startListening(intent)
    }

    /** Stops listening and throws away any pending result (no callbacks fire). */
    fun cancel() {
        recognizer?.cancel()
    }

    fun destroy() {
        recognizer?.destroy()
        recognizer = null
    }

    private val listener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) = Unit
        override fun onBeginningOfSpeech() = Unit
        override fun onBufferReceived(buffer: ByteArray?) = Unit
        override fun onEndOfSpeech() = Unit
        override fun onEvent(eventType: Int, params: Bundle?) = Unit

        override fun onRmsChanged(rmsdB: Float) {
            // rmsdB is roughly -2..10 dB; squash it into 0..1.
            callbacks.onRms(((rmsdB + 2f) / 12f).coerceIn(0f, 1f))
        }

        override fun onPartialResults(partialResults: Bundle?) {
            partialResults.firstResult()?.let(callbacks::onPartial)
        }

        override fun onResults(results: Bundle?) {
            callbacks.onFinal(results.firstResult().orEmpty())
        }

        override fun onError(error: Int) {
            callbacks.onError(error.toError())
        }
    }

    private fun Bundle?.firstResult(): String? =
        this?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()

    private fun Int.toError(): Error = when (this) {
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> Error.NoSpeech
        SpeechRecognizer.ERROR_NO_MATCH -> Error.NoMatch
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> Error.Busy
        SpeechRecognizer.ERROR_NETWORK,
        SpeechRecognizer.ERROR_NETWORK_TIMEOUT,
        SpeechRecognizer.ERROR_SERVER -> Error.Network
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> Error.Permission
        SpeechRecognizer.ERROR_AUDIO -> Error.Audio
        else -> Error.Other
    }
}
