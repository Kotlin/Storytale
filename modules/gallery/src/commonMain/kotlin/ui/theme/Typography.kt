package org.jetbrains.compose.storytale.gallery.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.gallery.compose.noCompositionLocalProvided

val LocalTypography = staticCompositionLocalOf<Typography> { noCompositionLocalProvided() }

val currentTypography: Typography
  @Composable
  @ReadOnlyComposable
  get() = LocalTypography.current

@Stable
class Typography(
  colorScheme: ColorScheme
) {
  val parameterText = TextStyle(
    fontSize = 17.sp,
    fontWeight = FontWeight.Medium,
    color = colorScheme.primaryText.copy(.67f),
  )
}
