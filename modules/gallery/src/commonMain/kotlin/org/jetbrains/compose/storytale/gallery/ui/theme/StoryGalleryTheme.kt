package org.jetbrains.compose.storytale.gallery.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import org.jetbrains.compose.storytale.gallery.compose.noCompositionLocalProvided

@Composable
fun StoryGalleryTheme(
    useDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (useDarkMode) ColorScheme.Dark else ColorScheme.Light
    val typography = Typography(colorScheme)
    val galleryDefaultTextStyle = LocalTextStyle.current.copy(
        color = colorScheme.primaryText,
    )
    CompositionLocalProvider(
        values = arrayOf(
            LocalColorScheme provides colorScheme,
            LocalTextStyle provides galleryDefaultTextStyle,
            LocalTypography provides typography,
            LocalIndication provides ripple(),
        ),
        content = content,
    )
}

val LocalCustomDensity = staticCompositionLocalOf<Density> { noCompositionLocalProvided() }

@Composable
inline fun UseCustomDensity(crossinline content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalDensity provides LocalCustomDensity.current) {
        content()
    }
}
