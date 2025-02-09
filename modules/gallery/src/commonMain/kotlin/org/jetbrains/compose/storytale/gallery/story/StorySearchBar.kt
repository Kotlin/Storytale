package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow

@Composable
fun StorySearchBar(
  text: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  BasicTextField(
    value = text,
    onValueChange = onValueChange,
    maxLines = 1,
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
      .background(Color.White),
  ) {
    CenterRow(Modifier.padding(12.dp)) {
      Box {
        if (text.isEmpty()) {
          Text(
            text = "Search Stories...",
            color = Color(0xFF6A7177),
            fontSize = 14.sp,
          )
        }
        it()
      }
    }
  }
}
