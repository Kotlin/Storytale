package ui.component

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
      content = {
        Box(Modifier.widthIn(min = minimumWidth)) { left() }
      }
    ).fastMap {
      it.measure(
        constraints.copy(
          maxWidth = dividerOffset.coerceIn(
            minimumValue = minimumWidth,
            maximumValue = layoutWidth.dp - minimumWidth
          ).roundToPx()
        )
      )
    }
    val divider = subcompose(
      slotId = HorizontalSplitPaneSlot.Divider,
      content = {
        Divider(
          modifier = Modifier.fillMaxHeight()
            .width(12.dp)
            .pointerInput(Unit) {
              detectDragGestures { _, dragAmount ->
                dividerOffset = (dividerOffset + dragAmount.x.toDp())
                  .coerceIn(
                    minimumValue = minimumWidth,
                    maximumValue = layoutWidth.dp - minimumWidth
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
          maxWidth = layoutWidth - leftArea.first().width - divider.first().width
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
