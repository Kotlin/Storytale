package org.jetbrains.compose.storytale

import androidx.compose.runtime.Composable
import kotlin.reflect.KProperty

val storiesStorage = mutableListOf<Story>()

fun story(content: @Composable Story.() -> Unit) = StoryDelegate(content)

class StoryDelegate(val content: @Composable Story.() -> Unit) {
  private lateinit var instance: Story

  operator fun getValue(thisRef: Any?, property: KProperty<*>): Story {
    return instance
  }

  operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): StoryDelegate {
    instance = Story(property.name, content).also(storiesStorage::add)
    return this
  }
}

class Story(val name: String, val content: @Composable Story.() -> Unit)