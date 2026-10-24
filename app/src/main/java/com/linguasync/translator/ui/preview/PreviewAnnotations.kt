package com.linguasync.translator.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview annotations used across the app. Change the settings here to change every preview.
 *
 *  - [LightDarkPreview] for small components (sized to their content)
 *  - [ScreenPreview]    for full screens (phone-sized)
 *
 * Usage:
 * ```
 * @LightDarkPreview
 * @Composable
 * private fun MyComponentPreview() {
 *     PreviewSurface { MyComponent() }
 * }
 * ```
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class LightDarkPreview

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(name = "Screen · Light", showBackground = true, widthDp = 411, heightDp = 891)
@Preview(
    name = "Screen · Dark",
    showBackground = true,
    widthDp = 411,
    heightDp = 891,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class ScreenPreview
