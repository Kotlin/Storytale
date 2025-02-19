package org.jetbrains.compose.storytale.gallery.ui.component.sheet

import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Density
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.storytale.gallery.compose.currentDensity

/**
 * Wrapping some methods for Material Design 3, such as show and hide,
 * eliminates the need to create a scope when using them, as well as a separate mutableStateOf boolean.
 */
@Stable
class StoryBottomSheetState(
    val sheetState: SheetState,
    private val scope: CoroutineScope,
) {
    var visible by mutableStateOf(false)
        private set

    fun show() {
        visible = true
    }

    fun hide() = scope.launch {
        sheetState.hide()
    }.invokeOnCompletion { visible = false }

    companion object {
        fun Saver(
            skipPartiallyExpanded: Boolean,
            confirmValueChange: (SheetValue) -> Boolean,
            density: Density,
            skipHiddenState: Boolean,
            scope: CoroutineScope,
        ) = Saver<StoryBottomSheetState, SheetValue>(
            save = {
                it.sheetState.currentValue
            },
            restore = {
                StoryBottomSheetState(
                    sheetState = SheetState(
                        initialValue = it,
                        confirmValueChange = confirmValueChange,
                        skipPartiallyExpanded = skipPartiallyExpanded,
                        positionalThreshold = { density.density },
                        velocityThreshold = { density.density },
                        skipHiddenState = skipHiddenState,
                    ),
                    scope = scope,
                )
            },
        )
    }
}

@Composable
fun rememberStoryBottomSheetState(
    skipPartiallyExpanded: Boolean = false,
    confirmValueChange: (SheetValue) -> Boolean = { true },
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded),
): StoryBottomSheetState {
    val scope = rememberCoroutineScope()
    val density = currentDensity
    return rememberSaveable(
        saver = StoryBottomSheetState.Saver(
            skipPartiallyExpanded = skipPartiallyExpanded,
            confirmValueChange = confirmValueChange,
            density = density,
            skipHiddenState = false,
            scope = scope,
        ),
    ) {
        StoryBottomSheetState(sheetState, scope)
    }
}
