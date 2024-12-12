package org.jetbrains.compose.storytale.gallery.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.gallery.ui.theme.currentColorScheme

@Composable
fun NumberChip(
  number: Int,
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(6.dp)
) = Box(
  modifier = modifier
    .clip(shape)
    .background(currentColorScheme.primaryText, shape)
) {
  Text(
    text = number.toString(),
    fontWeight = FontWeight.Bold,
    fontSize = 13.sp,
    modifier = Modifier.padding(horizontal = 5.5.dp, vertical = 2.dp),
    color = Color.White
  )
}
