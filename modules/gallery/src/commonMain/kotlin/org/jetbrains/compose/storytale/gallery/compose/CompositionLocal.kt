package org.jetbrains.compose.storytale.gallery.compose

inline fun <reified T> noCompositionLocalProvided(): T {
    error("CompositionLocal ${T::class.simpleName} not present")
}
