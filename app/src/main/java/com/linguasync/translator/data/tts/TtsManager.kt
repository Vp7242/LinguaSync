package com.linguasync.translator.data.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale
import java.util.UUID
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Speaks translations aloud with Android [TextToSpeech] (replaces `gTTS` + `playsound`).
 */
class TtsManager(context: Context) {

    private var ready = false
    private val engine: TextToSpeech = TextToSpeech(context.applicationContext) { status ->
        ready = status == TextToSpeech.SUCCESS
    }

    /**
     * Speaks [text] in [locale] and suspends until it has finished.
     * Returns false when the engine is not ready or has no voice for [locale].
     */
    suspend fun speak(text: String, locale: Locale): Boolean =
        suspendCancellableCoroutine { continuation ->
            if (!ready) {
                continuation.resume(false)
                return@suspendCancellableCoroutine
            }
            val result = engine.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                continuation.resume(false)
                return@suspendCancellableCoroutine
            }

            val utteranceId = UUID.randomUUID().toString()
            engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(id: String?) = Unit

                override fun onDone(id: String?) {
                    if (id == utteranceId && continuation.isActive) continuation.resume(true)
                }

                @Deprecated("Deprecated in Java")
                override fun onError(id: String?) {
                    if (id == utteranceId && continuation.isActive) continuation.resume(false)
                }

                override fun onStop(id: String?, interrupted: Boolean) {
                    if (id == utteranceId && continuation.isActive) continuation.resume(true)
                }
            })
            continuation.invokeOnCancellation { engine.stop() }

            val queued = engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
            if (queued == TextToSpeech.ERROR && continuation.isActive) continuation.resume(false)
        }

    fun stop() {
        engine.stop()
    }

    fun shutdown() {
        engine.stop()
        engine.shutdown()
    }
}
