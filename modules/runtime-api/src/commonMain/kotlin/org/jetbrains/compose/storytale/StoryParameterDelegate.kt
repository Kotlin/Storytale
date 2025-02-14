package org.jetbrains.compose.storytale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class StoryParameterDelegate<T>(
    private val story: Story,
    private val type: KClass<*>,
    private val defaultValue: T,
) {
    @Composable
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = story.nameToParameterMapping.getValue(property.name).state.value as T

    @Composable
    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>) = also {
        story.nameToParameterMapping.getOrPut(property.name) {
            StoryParameter(property.name, type) { remember { mutableStateOf(defaultValue) } }
        }
    }
}
