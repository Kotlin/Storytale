package org.jetbrains.compose.storytale

import androidx.compose.runtime.Composable
import kotlin.reflect.KProperty

val storiesStorage = mutableListOf<Story>()

inline fun story(
    code: String = "",
    group: String = "",
    crossinline content: @Composable Story.() -> Unit,
) = StoryDelegate({ content() }, group, code)

class StoryDelegate(
    private val content: @Composable Story.() -> Unit,
    private val group: String = "",
    private val code: String = "",
) {
    private lateinit var instance: Story

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Story {
        return instance
    }

    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): StoryDelegate {
        instance = Story(storiesStorage.size, property.name, group, code, content).also(storiesStorage::add)
        return this
    }
}
