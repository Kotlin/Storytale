package org.jetbrains.compose.storytale.gallery.ui.component

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactiveSize: Dp = 24.dp,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = ripple(
        bounded = false,
        radius = interactiveSize / 1.4f,
        color = Color.Unspecified,
    ),
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) = Box(
    modifier = modifier.clickable(
        enabled = enabled,
        onClick = onClick,
        indication = indication,
        interactionSource = interactionSource,
    ),
) { content() }
