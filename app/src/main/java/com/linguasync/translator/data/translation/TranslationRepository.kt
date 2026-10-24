package com.linguasync.translator.data.translation

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.tasks.await

/**
 * Translation and language detection through ML Kit (replaces `deep-translator`).
 * Language packs (~30 MB each) are downloaded once, then translation works offline.
 */
class TranslationRepository {

    private val languageIdentifier = LanguageIdentification.getClient()
    private val modelManager = RemoteModelManager.getInstance()
    private val translators = mutableMapOf<String, Translator>()

    /** Returns the ML Kit code of the language in [text], or null when it can't be determined. */
    suspend fun detectLanguage(text: String): String? {
        val tag = try {
            languageIdentifier.identifyLanguage(text).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return null
        }
        if (tag == "und") return null
        return TranslateLanguage.fromLanguageTag(tag.substringBefore('-'))
    }

    /**
     * Translates [text] from [sourceCode] to [targetCode].
     * [onDownloading] is called with true/false around a language pack download.
     */
    suspend fun translate(
        text: String,
        sourceCode: String,
        targetCode: String,
        onDownloading: (Boolean) -> Unit = {},
    ): Result<String> {
        if (sourceCode == targetCode) return Result.success(text)
        return try {
            val translator = translators.getOrPut("$sourceCode>$targetCode") {
                Translation.getClient(
                    TranslatorOptions.Builder()
                        .setSourceLanguage(sourceCode)
                        .setTargetLanguage(targetCode)
                        .build()
                )
            }
            val needsDownload = !isDownloaded(sourceCode) || !isDownloaded(targetCode)
            if (needsDownload) onDownloading(true)
            try {
                translator.downloadModelIfNeeded(DownloadConditions.Builder().build()).await()
            } finally {
                if (needsDownload) onDownloading(false)
            }
            Result.success(translator.translate(text).await())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun isDownloaded(code: String): Boolean {
        val model = TranslateRemoteModel.Builder(code).build()
        return try {
            modelManager.isModelDownloaded(model).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            false
        }
    }

    fun close() {
        translators.values.forEach { it.close() }
        translators.clear()
        languageIdentifier.close()
    }
}
