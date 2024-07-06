package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.generated.resources.Res
import org.jetbrains.compose.storytale.gallery.generated.resources.story_widget_icon
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import kotlin.math.roundToInt

@Composable
fun Story.StoryContentWrapper(
  storyName: String,
  content: @Composable Story.() -> Unit
) {
  var dragOffset by remember(storyName) {
    mutableStateOf(Offset.Zero)
  }
  Column(
    modifier = Modifier
      .offset { IntOffset(dragOffset.x.roundToInt(), dragOffset.y.roundToInt()) },
    horizontalAlignment = Alignment.Start
  ) {
    CenterRow(
      modifier = Modifier.pointerInput(Unit) {
        detectDragGestures { change, dragAmount ->
          change.consume()
          dragOffset += dragAmount
        }
      }
    ) {
      Icon(
        painter = painterResource(Res.drawable.story_widget_icon),
        contentDescription = null,
        modifier = Modifier.size(20.dp),
        tint = Color.Black,
      )
      Gap(8.dp)
      Text(
        text = storyName,
        fontSize = 18.sp,
        color = Color.Black,
      )
    }
    content()
  }
}
