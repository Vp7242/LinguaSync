package com.linguasync.translator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.linguasync.translator.feature.about.AboutRoute
import com.linguasync.translator.feature.translator.TranslatorRoute

/** Every screen of the app. */
enum class Destination { Translator, About }

/**
 * Decides which screen is visible. With only two screens a simple `when` is enough.
 *
 * TO ADD A SCREEN: add a [Destination], create a `feature/<name>/` folder with a `<Name>Route`
 * and `<Name>Screen`, then add a branch below.
 */
@Composable
fun AppNavigation() {
    var destination by rememberSaveable { mutableStateOf(Destination.Translator) }

    when (destination) {
        Destination.Translator -> TranslatorRoute(
            onOpenAbout = { destination = Destination.About },
        )
        Destination.About -> AboutRoute(
            onBack = { destination = Destination.Translator },
        )
    }
}
