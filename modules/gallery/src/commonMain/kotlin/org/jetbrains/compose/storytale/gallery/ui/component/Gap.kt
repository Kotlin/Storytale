package org.jetbrains.compose.storytale.gallery.ui.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.isSpecified

/**
 * A spacer that adds a vertical gap between elements in a column layout.
 *
 * @param height The height of the gap.
 * @param modifier The optional modifier to be applied to the spacer.
 */
@Composable
fun ColumnScope.Gap(height: Dp, modifier: Modifier = Modifier) =
  Spacer(modifier = modifier.height(height))

/**
 * A spacer that adds a horizontal gap between elements in a row layout.
 *
 * @param width The width of the gap.
 * @param modifier The optional modifier to be applied to the spacer.
 */
@Composable
fun RowScope.Gap(width: Dp, modifier: Modifier = Modifier) =
  Spacer(modifier = modifier.width(width))

/**
 * A spacer that fills the available space in a column layout.
 *
 * @param modifier The optional modifier to be applied to the spacer.
 */
@Composable
fun ColumnScope.FillGap(modifier: Modifier = Modifier) =
  Spacer(modifier = modifier.weight(1f))

/**
 * A spacer that fills the available space in a row layout.
 *
 * @param modifier The optional modifier to be applied to the spacer.
 */
@Composable
fun RowScope.FillGap(modifier: Modifier = Modifier) =
  Spacer(modifier = modifier.weight(1f))

/**
 * A spacer that adds a square gap of the specified size.
 *
 * @param size The size of the gap.
 * @param modifier The optional modifier to be applied to the spacer.
 */
@Composable
fun Gap(size: Dp, modifier: Modifier = Modifier) =
  Spacer(modifier = modifier.size(size))

/**
 * A spacer that adds a gap with the specified width and height.
 *
 * @param modifier The optional modifier to be applied to the spacer.
 * @param width The width of the gap. If not specified, the width will be unspecified.
 * @param height The height of the gap. If not specified, the height will be unspecified.
 */
@Composable
fun Gap(
  modifier: Modifier = Modifier,
  width: Dp = Dp.Unspecified,
  height: Dp = Dp.Unspecified
) = Spacer(
  modifier = modifier
    .run { if (width.isSpecified) width(width) else this }
    .run { if (height.isSpecified) height(height) else this }
)
