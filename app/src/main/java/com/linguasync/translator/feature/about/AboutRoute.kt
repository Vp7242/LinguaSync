package com.linguasync.translator.feature.about

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler

/** Original open-source project this app is based on. */
private const val ORIGINAL_PROJECT_URL = "https://github.com/SamirPaulb/real-time-voice-translator"

/**
 * The "stateful" side of the About screen: reads the app version, opens links and handles the
 * system back button. All visuals are in [AboutScreen].
 */
@Composable
fun AboutRoute(onBack: () -> Unit) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    val versionName = remember {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull().orEmpty()
    }

    BackHandler(onBack = onBack)

    AboutScreen(
        versionName = versionName,
        onBack = onBack,
        onOpenOriginalProject = { uriHandler.openUri(ORIGINAL_PROJECT_URL) },
    )
}
