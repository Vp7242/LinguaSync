package com.linguasync.translator.feature.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.linguasync.translator.R
import com.linguasync.translator.feature.about.components.AboutHeader
import com.linguasync.translator.feature.about.components.CreditSection
import com.linguasync.translator.feature.about.components.LanguageChips
import com.linguasync.translator.feature.about.components.SectionTitle
import com.linguasync.translator.feature.about.components.StepsList
import com.linguasync.translator.model.Language
import com.linguasync.translator.model.Languages
import com.linguasync.translator.ui.preview.PreviewSurface
import com.linguasync.translator.ui.preview.ScreenPreview
import com.linguasync.translator.ui.theme.Spacing

/**
 * About page. Stateless, so it previews without any setup.
 *
 * Sections, top to bottom: [AboutHeader], "How it works" ([StepsList]),
 * "Supported languages" ([LanguageChips]) and [CreditSection].
 * To add or reorder a section, edit the Column below.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    versionName: String,
    onBack: () -> Unit,
    onOpenOriginalProject: () -> Unit,
    modifier: Modifier = Modifier,
    languages: List<Language> = Languages.all,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.about_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.ExtraLarge, vertical = Spacing.Large),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AboutHeader(versionName = versionName)

            Spacer(Modifier.height(Spacing.ExtraLarge))
            SectionTitle(stringResource(R.string.about_how_title))
            StepsList(
                listOf(
                    stringResource(R.string.about_step_1),
                    stringResource(R.string.about_step_2),
                    stringResource(R.string.about_step_3),
                )
            )

            Spacer(Modifier.height(Spacing.ExtraLarge))
            SectionTitle(stringResource(R.string.about_languages_title))
            LanguageChips(languages)

            Spacer(Modifier.height(Spacing.ExtraLarge))
            CreditSection(onOpenOriginalProject = onOpenOriginalProject)
        }
    }
}

// ---- Previews ----------------------------------------------------------------------------

@ScreenPreview
@Composable
private fun AboutScreenPreview() {
    PreviewSurface {
        AboutScreen(versionName = "1.0.0", onBack = {}, onOpenOriginalProject = {})
    }
}
