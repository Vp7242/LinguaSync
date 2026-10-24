package com.linguasync.translator.feature.translator.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.linguasync.translator.R
import com.linguasync.translator.model.Language

/** Language name to show in the UI ("Auto-detect" is localised, the rest are fixed names). */
@Composable
fun Language.displayName(): String =
    if (isAuto) stringResource(R.string.language_auto) else name

/** Second line in the language list: the native name, or a hint for "Auto-detect". */
@Composable
fun Language.displaySubtitle(): String =
    if (isAuto) stringResource(R.string.language_auto_hint) else nativeName
