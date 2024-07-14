package org.jetbrains.compose.storytale.gallery.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import org.jetbrains.compose.storytale.gallery.compose.currentDensity

actual object ScreenSize {
  actual val width: Dp
    @Composable get() = with(currentDensity) {
      LocalWindowInfo.current.containerSize.width.toDp()
    }

  actual val height: Dp
    @Composable get() = with(currentDensity) {
      LocalWindowInfo.current.containerSize.width.toDp()
    }
}
