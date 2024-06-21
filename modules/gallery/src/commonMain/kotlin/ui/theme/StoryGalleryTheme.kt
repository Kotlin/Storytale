package org.jetbrains.compose.storytale.gallery.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun StoryGalleryTheme(
  useDarkMode: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (useDarkMode) ColorScheme.Dark else ColorScheme.Light
  val galleryDefaultTextStyle = LocalTextStyle.current.copy(
    color = colorScheme.primaryText
  )
  CompositionLocalProvider(
    values = arrayOf(
      LocalColorScheme provides colorScheme,
      LocalTextStyle provides galleryDefaultTextStyle
    ),
    content = content,
  )
}
