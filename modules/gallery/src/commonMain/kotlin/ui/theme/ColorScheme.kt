package ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import compose.noCompositionLocalProvided

@Immutable
data class ColorScheme(
  val primaryText: Color,
  val isLight: Boolean,
) {
  val isDark: Boolean inline get() = !isLight

  companion object {
    val Light = ColorScheme(
      primaryText = Color(0xFF252B30),
      isLight = true
    )

    val Dark = ColorScheme(
      primaryText = Color(0xFF252B30),
      isLight = false
    )
  }
}

val LocalColorScheme = staticCompositionLocalOf<ColorScheme> {
  noCompositionLocalProvided()
}

val currentColorScheme: ColorScheme
  @Composable
  @ReadOnlyComposable
  get() = LocalColorScheme.current

/**
 * Creates a color based on the current color scheme (light or dark).
 *
 * @param light The color to use when the current color scheme is light.
 * @param dark The color to use when the current color scheme is dark.
 * @return The appropriate color based on the current color scheme.
 */
@Composable
@ReadOnlyComposable
fun Color(light: Color, dark: Color): Color = when (LocalColorScheme.current.isLight) {
  true -> light
  false -> dark
}

/**
 * Creates a color based on the current color scheme (light or dark).
 *
 * @param light The color value to use when the current color scheme is light.
 * @param dark The color value to use when the current color scheme is dark.
 * @return The appropriate color based on the current color scheme.
 */
@Composable
@ReadOnlyComposable
fun Color(light: Long, dark: Long): Color = when (LocalColorScheme.current.isLight) {
  true -> Color(light)
  false -> Color(dark)
}
