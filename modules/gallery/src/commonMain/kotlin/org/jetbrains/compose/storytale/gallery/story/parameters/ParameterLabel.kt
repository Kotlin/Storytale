package org.jetbrains.compose.storytale.gallery.story.parameters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap

@Composable
fun ParameterLabel(
    name: String,
    modifier: Modifier = Modifier,
) = Box(
    modifier = modifier
        .clip(RoundedCornerShape(5.dp))
        .background(Color(0xFFF1F5F9)),
) {
    Text(
        text = name,
        color = Color(0xFF64748B),
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
    )
}

@Composable
fun ParameterLabel2(
    name: String,
    modifier: Modifier = Modifier,
) = Box(
    modifier = modifier
        .clip(RoundedCornerShape(5.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant),
) {
    Text(
        text = name,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 8.sp,
        modifier = Modifier.padding(4.dp),
    )
}


@Composable
fun ParameterHeader(
    name: String,
    type: String,
    modifier: Modifier = Modifier
) {
    CenterRow(modifier) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
        )
        Gap(6.dp)
        ParameterLabel2(type)
    }
}

@Composable
fun ParameterDescription(
    description: String
) {
    if (description.isNotEmpty()) {
        Gap(8.dp)
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
