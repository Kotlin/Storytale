package org.jetbrains.compose.storytale.gallery.ui.component

import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap

enum class HorizontalSplitPaneSlot {
  Left,
  Divider,
  Right
}

@Composable
fun HorizontalSplitPane(
  modifier: Modifier = Modifier,
  minimumWidth: Dp = 250.dp,
  left: @Composable () -> Unit,
  right: @Composable () -> Unit
) {
  var dividerOffset by remember { mutableStateOf(minimumWidth) }
  SubcomposeLayout(
    modifier = modifier,
  ) { constraints ->
    val layoutWidth = constraints.maxWidth
    val leftArea = subcompose(
      slotId = HorizontalSplitPaneSlot.Left,
      content = { left() }
    ).fastMap {
      it.measure(
        constraints.copy(
          maxWidth = dividerOffset.coerceIn(
            minimumValue = 0.dp,
            maximumValue = layoutWidth.toDp() - minimumWidth
          ).roundToPx()
        )
      )
    }

    val divider = subcompose(
      slotId = HorizontalSplitPaneSlot.Divider,
      content = {
        Divider(
          modifier = Modifier.fillMaxHeight()
            .clip(CircleShape)
            .width(6.dp)
            .pointerInput(layoutWidth) {
              detectDragGestures { _, dragAmount ->
                dividerOffset = (dividerOffset + dragAmount.x.toDp())
                  .coerceIn(
                    minimumValue = 0.dp,
                    maximumValue = layoutWidth.toDp() - minimumWidth
                  )
              }
            },
          color = Color(0xFFDADADA)
        )
      }
    ).fastMap { it.measure(constraints) }

    val rightArea = subcompose(
      slotId = HorizontalSplitPaneSlot.Right,
      content = right
    ).fastMap {
      it.measure(
        constraints.copy(
          maxWidth = (layoutWidth - leftArea.first().width - divider.first().width)
            .coerceAtLeast(0)
        )
      )
    }
    layout(constraints.maxWidth, constraints.maxHeight) {
      leftArea.fastForEach {
        it.place(0, 0)
      }
      divider.fastForEach {
        it.place(leftArea.first().width, 0)
      }
      rightArea.fastForEach {
        it.place(divider.first().width + leftArea.first().width, 0)
      }
    }
  }
}
