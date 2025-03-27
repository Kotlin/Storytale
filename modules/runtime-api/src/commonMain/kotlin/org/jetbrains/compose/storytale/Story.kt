package org.jetbrains.compose.storytale

import androidx.compose.runtime.Composable

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

    @Composable
    inline fun <reified T> parameter(defaultValue: T) = StoryParameterDelegate(this, T::class, defaultValue)
}
