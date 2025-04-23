package org.jetbrains.compose.storytale.gallery.story.parameters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.storytale.gallery.generated.resources.Res
import org.jetbrains.compose.storytale.gallery.generated.resources.arrow_back
import org.jetbrains.compose.storytale.gallery.material3.ParameterDescription
import org.jetbrains.compose.storytale.gallery.material3.ParameterHeader
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap

@Suppress("ktlint:compose:mutable-state-param-check")
@Composable
fun ListParameter(
    parameterName: String,
    selectedValueIndex: MutableState<Int>,
    values: List<Any?>,
    modifier: Modifier = Modifier,
    label: String? = null,
    description: String? = null,
) = Column(modifier) {
    var isSheetVisible by remember { mutableStateOf(false) }
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedValueIndex.value)

    ParameterHeader(
        name = parameterName,
        type = label ?: "",
    )
    Gap(12.dp)
    Row {
        Box(modifier = Modifier.weight(1f)) {
            LazyRow(
                state = listState,
                userScrollEnabled = true,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                itemsIndexed(values) { index, item ->
                    InputChip(
                        selected = index == selectedValueIndex.value,
                        onClick = { selectedValueIndex.value = index },
                        label = {
                            Text(
                                text = item.toString(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                    )
                }
            }
        }

        if (listState.canScrollForward || listState.canScrollBackward) {
            LaunchedEffect(selectedValueIndex.value) {
                listState.animateScrollToItem(selectedValueIndex.value)
            }
            InputChip(
                selected = false,
                onClick = { isSheetVisible = !isSheetVisible },
                label = {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_back),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp).rotate(180f),
                    )
                },
            )
        }
    }

    if (isSheetVisible) {
        SelectionSheet(onDismissRequest = { isSheetVisible = false }, values, selectedValueIndex)
    }

    ParameterDescription(description ?: "")
}

@Suppress("ktlint:compose:mutable-state-param-check")
@Composable
private fun <T> SelectionSheet(
    onDismissRequest: () -> Unit,
    values: List<T>,
    selectedValueIndex: MutableState<Int>,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = { BottomSheetDragHandle() },
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = (24 * 2).dp),
        contentWindowInsets = { WindowInsets.displayCutout },
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .selectableGroup(),
            state = rememberLazyListState(),
        ) {
            itemsIndexed(values) { index, item ->
                val selected = selectedValueIndex.value == index
                CenterRow(
                    modifier = Modifier
                        .selectable(
                            selected = selected,
                            onClick = { selectedValueIndex.value = index },
                            role = Role.RadioButton,
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    RadioButton(
                        selected = selected,
                        onClick = null,
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomSheetDragHandle(
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
                cornerRadius = CornerRadius(barHeightPx / 2),
            )
        }
        .fillMaxWidth()
        .height(height),
)

private val height: Dp = 24.dp
private val barWidth: Dp = 32.dp
private val barHeight: Dp = 4.dp
