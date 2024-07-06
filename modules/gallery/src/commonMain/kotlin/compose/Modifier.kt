package org.jetbrains.compose.storytale.gallery.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@Stable
fun Modifier.clickableWithoutRipple(
  enabled: Boolean = true,
  onClick: () -> Unit
) = if (enabled) clickable(
  onClick = onClick,
  indication = null,
  interactionSource = MutableInteractionSource()
) else Modifier

inline fun Modifier.thenIf(
  condition: Boolean,
  modifier: Modifier.() -> Modifier,
): Modifier = if (condition) this.then(modifier()) else this