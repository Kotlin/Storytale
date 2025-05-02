package org.jetbrains.compose.storytale.gallery.compose

import androidx.compose.runtime.staticCompositionLocalOf

inline fun <reified T> noCompositionLocalProvided(): T {
    error("CompositionLocal ${T::class.simpleName} not present")
}

val LocalIsEmbeddedView = staticCompositionLocalOf { false }
