package org.jetbrains.compose.storytale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import kotlin.reflect.KClass

class StoryParameter<T>(
    val name: String,
    val type: KClass<*>,
    private val initializeMutableState: @Composable () -> MutableState<T>,
) {
    private var _state: MutableState<T>? = null
    val state: MutableState<T>
        @Composable get() {
            var state = _state
            if (state == null) {
                state = initializeMutableState()
                _state = state
            }
            return state
        }
}
