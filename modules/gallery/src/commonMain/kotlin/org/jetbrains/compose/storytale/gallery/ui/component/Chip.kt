package org.jetbrains.compose.storytale.gallery.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.gallery.compose.thenIf
import org.jetbrains.compose.storytale.gallery.ui.theme.currentColorScheme
import org.jetbrains.compose.storytale.gallery.ui.theme.currentTypography

@Composable
internal fun Chip(
    label: String,
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Chip(selected, onClick, modifier) {
        Text(
            text = label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
            color = if (selected) Color.White else currentColorScheme.primaryText,
            style = currentTypography.parameterText,
        )
    }
}

@Composable
internal fun Chip(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val backgroundColor = if (selected) currentColorScheme.primaryText else Color.Transparent
    val borderColor = if (selected) currentColorScheme.primaryText else currentColorScheme.primaryText.copy(alpha = 0.6f)
    Box(
        modifier = modifier
            .heightIn(min = 32.dp)
            .clip(ChipShape)
            .thenIf(onClick != null) {
                selectable(selected, role = Role.RadioButton, onClick = onClick!!)
            }
            .border(1.dp, borderColor, ChipShape)
            .background(backgroundColor, ChipShape)
            .padding(horizontal = 12.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

private val ChipShape = RoundedCornerShape(8.dp)
