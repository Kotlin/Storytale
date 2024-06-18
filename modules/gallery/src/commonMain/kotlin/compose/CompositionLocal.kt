package compose

import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle

val currentTextStyle: TextStyle
  @Composable
  @ReadOnlyComposable
  get() = LocalTextStyle.current

inline fun <reified T> noCompositionLocalProvided(): T {
  error("CompositionLocal ${T::class.simpleName} not present")
}
