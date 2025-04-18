package org.jetbrains.compose.storytale

import androidx.compose.runtime.MutableState
import kotlin.reflect.KClass

class StoryParameter<T>(
    val name: String,
    val type: KClass<*>,
    val values: List<*>?,
    val label: String?,
    val state: MutableState<T>,
)
