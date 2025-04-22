package org.jetbrains.compose.storytale

import androidx.compose.runtime.Composable
import kotlin.enums.enumEntries

data class Story(
    val id: Int,
    val name: String,
    val group: String,
    val code: String,
    val content: @Composable Story.() -> Unit,
) {
    @PublishedApi
    internal val nameToParameterMapping = linkedMapOf<String, StoryParameter<*>>() // using linkedMap to keep the order
    val parameters inline get() = nameToParameterMapping.values.toList()

    inline fun <reified T> parameter(defaultValue: T) = StoryParameterDelegate(this, T::class, defaultValue)

    inline fun <reified T> parameter(values: List<T>, defaultValueIndex: Int = 0, label: String? = null) =
        StoryListParameterDelegate(this, T::class, values, defaultValueIndex, label)

    inline fun <reified T : Enum<T>> parameter(defaultValue: T, label: String? = null): StoryListParameterDelegate<T> {
        val enumEntries = enumEntries<T>()
        return StoryListParameterDelegate(this, T::class, enumEntries, enumEntries.indexOf(defaultValue), label)
    }
}
