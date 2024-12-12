package org.jetbrains.compose.storytale.gallery.ui.component.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue.Hidden
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 *
 * A simple wrapper for the Material 3 ModalBottomSheet,
 * which eliminates unnecessary variable declarations and CoroutineScope
 *
 * Modal bottom sheets are used as an alternative to inline menus or simple dialogs on mobile,
 * especially when offering a long list of action items, or when items require longer descriptions
 * and icons. Like dialogs, modal bottom sheets appear in front of app content, disabling all other
 * app functionality when they appear, and remaining on screen until confirmed, dismissed, or a
 * required action has been taken.
 *
 * ![Bottom sheet
 * image](https://developer.android.com/images/reference/androidx/compose/material3/bottom_sheet.png)
 *
 * A simple example of a modal bottom sheet looks like this:
 *
 * @sample androidx.compose.material3.samples.ModalBottomSheetSample
 *
 * @param onDismissRequest Executes when the user clicks outside of the bottom sheet, after sheet
 *   animates to [Hidden].
 * @param modifier Optional [Modifier] for the bottom sheet.
 * @param sheetState The state of the bottom sheet.
 * @param sheetMaxWidth [Dp] that defines what the maximum width the sheet will take. Pass in
 *   [Dp.Unspecified] for a sheet that spans the entire screen width.
 * @param shape The shape of the bottom sheet.
 * @param containerColor The color used for the background of this bottom sheet
 * @param contentColor The preferred color for content inside this bottom sheet. Defaults to either
 *   the matching content color for [containerColor], or to the current [LocalContentColor] if
 *   [containerColor] is not a color from the theme.
 * @param tonalElevation when [containerColor] is [ColorScheme.surface], a translucent primary color
 *   overlay is applied on top of the container. A higher tonal elevation value will result in a
 *   darker color in light theme and lighter color in dark theme. See also: [Surface].
 * @param scrimColor Color of the scrim that obscures content when the bottom sheet is open.
 * @param dragHandle Optional visual marker to swipe the bottom sheet.
 * @param contentWindowInsets window insets to be passed to the bottom sheet content via
 *   [PaddingValues] params.
 * @param properties [ModalBottomSheetProperties] for further customization of this modal bottom
 *   sheet's window behavior.
 * @param content The content to be displayed inside the bottom sheet.
 */
@Composable
fun StoryBottomSheet(
  onDismissRequest: () -> Unit,
  modifier: Modifier = Modifier,
  sheetState: StoryBottomSheetState = rememberStoryBottomSheetState(),
  contentPadding: PaddingValues = PaddingValues(0.dp),
  sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
  shape: Shape = BottomSheetDefaults.ExpandedShape,
  containerColor: Color = BottomSheetDefaults.ContainerColor,
  contentColor: Color = contentColorFor(containerColor),
  tonalElevation: Dp = 0.dp,
  scrimColor: Color = BottomSheetDefaults.ScrimColor,
  dragHandle: @Composable (() -> Unit)? = { StoryBottomSheetDragHandle() },
  contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
  properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
  content: @Composable ColumnScope.() -> Unit,
) {
  if (sheetState.visible) {
    ModalBottomSheet(
      onDismissRequest = onDismissRequest,
      modifier = modifier,
      sheetState = sheetState.sheetState,
      sheetMaxWidth = sheetMaxWidth,
      shape = shape,
      containerColor = containerColor,
      contentColor = contentColor,
      tonalElevation = tonalElevation,
      scrimColor = scrimColor,
      dragHandle = dragHandle,
      contentWindowInsets = contentWindowInsets,
      properties = properties,
      content = {
        Column(Modifier.padding(contentPadding)) { content() }
      }
    )
  }
}

@Composable
private fun StoryBottomSheetDragHandle(
  modifier: Modifier = Modifier,
  color: Color = Color(0xFFD2D7E6),
) = Spacer(
  modifier = modifier
    .drawBehind {
      val barWidthPx = barWidth.toPx()
      val barHeightPx = barHeight.toPx()
      val x = size.width / 2 - barWidthPx / 2
      val y = size.height / 2 - barHeightPx / 2
      drawRoundRect(
        color = color,
        topLeft = Offset(x, y),
        size = Size(barWidthPx, barHeightPx),
        cornerRadius = CornerRadius(barHeightPx / 2)
      )
    }
    .fillMaxWidth()
    .height(height)
)

private val height: Dp = 24.dp
private val barWidth: Dp = 32.dp
private val barHeight: Dp = 4.dp
