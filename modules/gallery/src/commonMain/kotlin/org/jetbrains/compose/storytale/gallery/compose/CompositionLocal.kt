package org.jetbrains.compose.storytale.gallery.compose

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density

val currentDensity: Density
    @Composable
    @ReadOnlyComposable
    get() = LocalDensity.current

val currentTextStyle: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = LocalTextStyle.current

inline fun <reified T> noCompositionLocalProvided(): T {
    error("CompositionLocal ${T::class.simpleName} not present")
}
