package org.jetbrains.compose.storytale.gallery.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import org.jetbrains.compose.storytale.gallery.compose.noCompositionLocalProvided

val LocalCustomDensity = staticCompositionLocalOf<Density> { noCompositionLocalProvided() }

@Composable
inline fun UseCustomDensity(crossinline content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalDensity provides LocalCustomDensity.current) {
        content()
    }
}
