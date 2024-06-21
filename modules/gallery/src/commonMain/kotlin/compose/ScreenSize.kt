package org.jetbrains.compose.storytale.gallery.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

expect object ScreenSize {
  @get:Composable
  val width: Dp

  @get:Composable
  val height: Dp
}

val ScreenSize.isMobile @Composable inline get() = width <= 480.dp
