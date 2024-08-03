package org.jetbrains.compose.storytale.gallery.ui.component

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.gallery.compose.currentDensity

@Composable
fun HorizontalSplitPane(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  var dividerOffset by remember { mutableStateOf(0.dp) }
  Layout(
    modifier = modifier,
    content = {
      content()
      VerticalSplitDivider {
        dividerOffset += it
      }
    }
  ) { measurables, constraints ->
    require(measurables.size == 3) {
      "HorizontalSplitPane only supports plays with two @Composable content, " +
        "the @Composable functions to the left and right of the splitter line."
    }
    val dividerWidth = measurables[2].maxIntrinsicWidth(constraints.maxHeight)
    val firstPlaceable = measurables[0].measure(
      constraints.copy(
        maxWidth = (250.dp + dividerOffset).coerceIn(
          minimumValue = 0.dp,
          maximumValue = constraints.maxWidth.toDp()
        ).roundToPx()
      )
    )
    val secondWidth = (constraints.maxWidth - firstPlaceable.width - dividerWidth)
      .coerceIn(minimumValue = 0, maximumValue = constraints.maxWidth)
    val secondPlaceable = measurables[1].measure(
      Constraints(
        minWidth = secondWidth,
        maxWidth = secondWidth,
        minHeight = constraints.maxHeight,
        maxHeight = constraints.maxHeight
      )
    )
    val splitterPlaceable = measurables[2].measure(constraints)
    layout(constraints.maxWidth, constraints.maxHeight) {
      firstPlaceable.place(0, 0)
      secondPlaceable.place(
        x = firstPlaceable.width + dividerWidth,
        y = 0
      )
      splitterPlaceable.place(firstPlaceable.width, 0)
    }
  }
}

@Composable
private fun VerticalSplitDivider(
  modifier: Modifier = Modifier,
  shape: Shape = CircleShape,
  color: Color = Color(0xFFDADADA),
  width: Dp = 4.dp,
  onDrag: (Dp) -> Unit
) {
  val density = currentDensity
  Divider(
    modifier = modifier.fillMaxHeight()
      .clip(shape)
      .width(width)
      .draggable(
        state = rememberDraggableState { offset ->
          with(density) {
            onDrag(offset.toDp())
          }
        },
        orientation = Orientation.Horizontal,
        startDragImmediately = true
      ),
    color = color
  )
}
