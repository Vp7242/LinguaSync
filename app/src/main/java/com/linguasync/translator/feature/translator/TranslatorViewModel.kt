package com.linguasync.translator.feature.translator

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.nl.translate.TranslateLanguage
import com.linguasync.translator.R
import com.linguasync.translator.data.speech.SpeechRecognizerManager
import com.linguasync.translator.data.translation.TranslationRepository
import com.linguasync.translator.data.tts.TtsManager
import com.linguasync.translator.model.Language
import com.linguasync.translator.model.Languages
import com.linguasync.translator.model.ListeningStatus
import com.linguasync.translator.model.TranslationEntry
import java.util.Locale
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Runs the pipeline: microphone -> speech-to-text -> translation -> text-to-speech,
 * and keeps listening until the user stops (button or saying "stop" / "exit").
 */
class TranslatorViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(TranslatorUiState())
    val state: StateFlow<TranslatorUiState> = _state.asStateFlow()

    private val repository = TranslationRepository()
    private val tts = TtsManager(application)
    private val speech = SpeechRecognizerManager(application, object : SpeechRecognizerManager.Callbacks {
        override fun onRms(level: Float) = handleRms(level)
        override fun onPartial(text: String) = handlePartial(text)
        override fun onFinal(text: String) = handleFinal(text)
        override fun onError(error: SpeechRecognizerManager.Error) = handleError(error)
    })

    private var pipelineJob: Job? = null
    private var restartJob: Job? = null
    private var nextEntryId = 0L

    // ---- Actions from the UI -------------------------------------------------------------

    fun setInputLanguage(language: Language) = _state.update { it.copy(inputLanguage = language) }

    fun setOutputLanguage(language: Language) = _state.update { it.copy(outputLanguage = language) }

    /** Swaps input and output. Not possible while the input is "Auto-detect". */
    fun swapLanguages() = _state.update {
        if (it.inputLanguage.isAuto) it
        else it.copy(inputLanguage = it.outputLanguage, outputLanguage = it.inputLanguage)
    }

    fun toggleSession() {
        if (_state.value.isSessionActive) stopSession() else startSession()
    }

    fun startSession() {
        if (_state.value.isSessionActive) return
        if (!speech.isAvailable) {
            showError(R.string.error_recognition_unavailable)
            return
        }
        _state.update { it.copy(isSessionActive = true, error = null) }
        beginListening()
    }

    fun stopSession() {
        pipelineJob?.cancel()
        restartJob?.cancel()
        speech.cancel()
        tts.stop()
        _state.update {
            it.copy(
                isSessionActive = false,
                status = ListeningStatus.Idle,
                partialText = "",
                micLevel = 0f,
                isDownloadingModel = false,
            )
        }
    }

    fun onPermissionDenied() = showError(R.string.error_permission)

    fun dismissError() = _state.update { it.copy(error = null) }

    fun clearHistory() = _state.update {
        it.copy(history = emptyList(), recognizedText = "", translatedText = "")
    }

    /** Plays a past translation again (pauses a running session first). */
    fun replay(entry: TranslationEntry) {
        stopSession()
        pipelineJob = viewModelScope.launch {
            _state.update { it.copy(status = ListeningStatus.Speaking) }
            val tag = entry.targetSpeechTag
            val spoken = tag != null && tts.speak(entry.translatedText, Locale.forLanguageTag(tag))
            if (!spoken) showError(R.string.error_tts_unavailable, entry.targetLanguageName)
            _state.update { it.copy(status = ListeningStatus.Idle) }
        }
    }

    // ---- Pipeline ------------------------------------------------------------------------

    private fun beginListening() {
        _state.update { it.copy(status = ListeningStatus.Listening, partialText = "", micLevel = 0f) }
        speech.start(_state.value.inputLanguage.speechTag)
    }

    private fun scheduleRestart() {
        restartJob?.cancel()
        restartJob = viewModelScope.launch {
            delay(350)
            if (_state.value.isSessionActive) beginListening()
        }
    }

    private fun handleRms(level: Float) {
        if (_state.value.status == ListeningStatus.Listening) {
            _state.update { it.copy(micLevel = level) }
        }
    }

    private fun handlePartial(text: String) {
        if (!_state.value.isSessionActive) return
        _state.update { it.copy(partialText = text, error = null) }
    }

    private fun handleFinal(text: String) {
        if (!_state.value.isSessionActive) return
        val spoken = text.trim()
        when {
            spoken.isEmpty() -> scheduleRestart()
            spoken.lowercase(Locale.ROOT) in STOP_COMMANDS -> stopSession()
            else -> translateAndSpeak(spoken)
        }
    }

    private fun handleError(error: SpeechRecognizerManager.Error) {
        if (!_state.value.isSessionActive) return
        when (error) {
            SpeechRecognizerManager.Error.NoSpeech -> scheduleRestart()
            SpeechRecognizerManager.Error.NoMatch -> {
                showError(R.string.error_not_understood)
                scheduleRestart()
            }
            SpeechRecognizerManager.Error.Busy -> {
                speech.cancel()
                scheduleRestart()
            }
            SpeechRecognizerManager.Error.Network -> failSession(R.string.error_network)
            SpeechRecognizerManager.Error.Permission -> failSession(R.string.error_permission)
            SpeechRecognizerManager.Error.Audio -> failSession(R.string.error_audio)
            SpeechRecognizerManager.Error.Other -> failSession(R.string.error_generic)
        }
    }

    private fun translateAndSpeak(spoken: String) {
        pipelineJob?.cancel()
        pipelineJob = viewModelScope.launch {
            val snapshot = _state.value
            _state.update {
                it.copy(
                    status = ListeningStatus.Processing,
                    recognizedText = spoken,
                    partialText = "",
                    micLevel = 0f,
                    error = null,
                )
            }

            val sourceCode = if (snapshot.inputLanguage.isAuto) {
                repository.detectLanguage(spoken) ?: deviceLanguageCode()
            } else {
                snapshot.inputLanguage.code
            }

            val result = repository.translate(
                text = spoken,
                sourceCode = sourceCode,
                targetCode = snapshot.outputLanguage.code,
                onDownloading = { downloading ->
                    _state.update { it.copy(isDownloadingModel = downloading) }
                },
            )

            result.onSuccess { translated ->
                val entry = TranslationEntry(
                    id = nextEntryId++,
                    originalText = spoken,
                    translatedText = translated,
                    sourceLanguageName = languageName(sourceCode, snapshot.inputLanguage),
                    targetLanguageName = snapshot.outputLanguage.name,
                    targetSpeechTag = snapshot.outputLanguage.speechTag,
                )
                _state.update {
                    it.copy(
                        translatedText = translated,
                        history = it.history + entry,
                        status = ListeningStatus.Speaking,
                    )
                }
                val locale = snapshot.outputLanguage.locale ?: Locale.ENGLISH
                if (!tts.speak(translated, locale)) {
                    showError(R.string.error_tts_unavailable, snapshot.outputLanguage.name)
                }
            }.onFailure {
                showError(R.string.error_translation)
            }

            _state.update { it.copy(status = ListeningStatus.Idle, isDownloadingModel = false) }
            if (_state.value.isSessionActive) beginListening()
        }
    }

    // ---- Helpers -------------------------------------------------------------------------

    private fun failSession(@StringRes message: Int) {
        stopSession()
        showError(message)
    }

    private fun showError(@StringRes message: Int, vararg args: Any) {
        val text = getApplication<Application>().getString(message, *args)
        _state.update { it.copy(error = text) }
    }

    private fun deviceLanguageCode(): String =
        TranslateLanguage.fromLanguageTag(Locale.getDefault().language) ?: TranslateLanguage.ENGLISH

    private fun languageName(code: String, selected: Language): String {
        if (!selected.isAuto) return selected.name
        return Languages.all.firstOrNull { it.code == code }?.name
            ?: Locale.forLanguageTag(code).getDisplayLanguage(Locale.ENGLISH)
    }

    override fun onCleared() {
        pipelineJob?.cancel()
        restartJob?.cancel()
        speech.destroy()
        tts.shutdown()
        repository.close()
    }

    private companion object {
        /** Voice commands (from the original app) that end a session. */
        val STOP_COMMANDS = setOf("stop", "exit")
    }
}
