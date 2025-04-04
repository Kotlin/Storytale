package org.jetbrains.compose.storytale

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class StoryParameterDelegate<T>(
    private val story: Story,
    private val type: KClass<*>,
    private val defaultValue: T,
) {
    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = story.nameToParameterMapping.getValue(property.name).state.value as T

    @Suppress("UNCHECKED_CAST")
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        (story.nameToParameterMapping.getValue(property.name).state as MutableState<T>).value = value
    }

    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>) = also {
        story.nameToParameterMapping.getOrPut(property.name) {
            StoryParameter(property.name, type) { mutableStateOf(defaultValue) }
        }
    }
}
