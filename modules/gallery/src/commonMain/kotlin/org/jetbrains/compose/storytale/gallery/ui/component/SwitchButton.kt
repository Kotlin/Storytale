package org.jetbrains.compose.storytale.gallery.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.gallery.compose.clickableWithoutRipple
import org.jetbrains.compose.storytale.gallery.compose.thenIf

@Composable
fun SwitchButton(
    checked: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) = BoxWithConstraints(
    modifier = modifier
        .size(width = 48.dp, height = 28.dp)
        .background(
            color = animateColorAsState(
                targetValue = if (checked) Color(0xFF00AF54) else Color(0xFFABABAB),
                animationSpec = tween(delayMillis = 10),
            ).value,
            shape = RoundedCornerShape(percent = 50),
        )
        .thenIf(!enabled) { alpha(0.54f) }
        .clickableWithoutRipple(enabled) { onValueChange(!checked) },
) {
    val padding = 2.5.dp
    val thumbSize = 24.dp
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) maxWidth - thumbSize - padding else padding,
        animationSpec = spring(dampingRatio = 0.58f, stiffness = 620f),
    )
    Spacer(
        modifier = Modifier
            .size(thumbSize)
            .graphicsLayer { translationX = thumbOffset.toPx() }
            .shadow(
                elevation = if (enabled) 4.dp else 0.dp,
                shape = CircleShape,
            )
            .background(
                color = Color.White,
                shape = CircleShape,
            )
            .align(Alignment.CenterStart),
    )
}
