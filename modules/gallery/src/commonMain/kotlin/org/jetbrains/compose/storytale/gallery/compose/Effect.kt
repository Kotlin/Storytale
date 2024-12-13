package org.jetbrains.compose.storytale.gallery.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import kotlinx.coroutines.CoroutineScope

@Composable
@NonRestartableComposable
fun <T : Any?> LaunchedValueEffect(
  key: T,
  block: suspend CoroutineScope.(T) -> Unit
) = LaunchedEffect(key) {
  block(key)
}
