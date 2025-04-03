package org.jetbrains.compose.storytale

import androidx.compose.runtime.MutableState
import kotlin.reflect.KClass

class StoryParameter<T>(
    val name: String,
    val type: KClass<*>,
    private val initializeMutableState: () -> MutableState<T>,
) {
    private var _state: MutableState<T>? = null
    val state: MutableState<T>
        get() {
            var state = _state
            if (state == null) {
                state = initializeMutableState()
                _state = state
            }
            return state
        }
}
