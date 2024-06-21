package org.jetbrains.compose.storytale.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap

@Composable
fun StoryTextInput(
  modifier: Modifier = Modifier
) {
  var text by remember { mutableStateOf("") }
  BasicTextField(
    value = text,
    onValueChange = {
      text = it
    },
    maxLines = 1,
    modifier = modifier.clip(RoundedCornerShape(12.dp))
      .background(Color(0xFFF9F9FB))
  ) {
    CenterRow(Modifier.padding(8.dp)) {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "Search",
        modifier = Modifier.size(20.dp),
        tint = Color(0xFF6A7177).copy(.69f)
      )
      Gap(10.dp)
      Box {
        if (text.isEmpty()) {
          Text(
            text = "Search Stories...",
            color = Color(0xFF6A7177),
            fontSize = 14.sp
          )
        }
        it()
      }
    }
  }
}
