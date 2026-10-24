package com.linguasync.translator.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Brand colours live in Color.kt. Edit the schemes below to re-map them to Material roles.
private val LightColors = lightColorScheme(
    primary = Indigo40,
    primaryContainer = IndigoContainerLight,
    secondary = Teal40,
    secondaryContainer = TealContainerLight,
    error = Red40,
    errorContainer = RedContainerLight,
    background = SurfaceLight,
    surface = SurfaceLight,
)

private val DarkColors = darkColorScheme(
    primary = Indigo80,
    primaryContainer = IndigoContainerDark,
    secondary = Teal80,
    secondaryContainer = TealContainerDark,
    error = Red80,
    errorContainer = RedContainerDark,
    background = SurfaceDark,
    surface = SurfaceDark,
)

/**
 * App theme.
 *
 * @param dynamicColor use the wallpaper-based colours on Android 12+. Set to false to always
 *                     use the brand colours (previews do this so they look the same everywhere).
 */
@Composable
fun LinguaSyncTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
