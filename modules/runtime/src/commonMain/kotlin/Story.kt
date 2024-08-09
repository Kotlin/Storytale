package org.jetbrains.compose.storytale

import androidx.compose.runtime.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

val storiesStorage = mutableListOf<Story>()

fun story(content: @Composable Story.() -> Unit) = StoryDelegate(content)

class StoryDelegate(val content: @Composable Story.() -> Unit) {
  private lateinit var instance: Story

  operator fun getValue(thisRef: Any?, property: KProperty<*>): Story {
    return instance
  }

  operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): StoryDelegate {
    instance = Story(storiesStorage.size, property.name, content).also(storiesStorage::add)
    return this
  }
}

data class Story(
  val id: Int,
  val name: String,
  val content: @Composable Story.() -> Unit
) {
  internal val nameToParameterMapping = hashMapOf<String, StoryParameter<*>>()
  val parameters: Iterable<StoryParameter<*>> get() = nameToParameterMapping.values

  @Composable
  inline fun <reified T> parameter(defaultValue: T) =
    StoryParameterDelegate(this, T::class, defaultValue)
}

class StoryParameter<T>(
  val name: String,
  val type: KClass<*>,
  private val initializeMutableState: @Composable () -> MutableState<T>
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

class StoryParameterDelegate<T>(
  private val story: Story,
  private val type: KClass<*>,
  private val defaultValue: T,
) {
  @Composable
  operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
    @Suppress("UNCHECKED_CAST")
    story.nameToParameterMapping.getValue(property.name).state.value as T

  @Composable
  operator fun provideDelegate(thisRef: Any?, property: KProperty<*>) = also {
    story.nameToParameterMapping.getOrPut(property.name) {
      StoryParameter(property.name, type) { remember { mutableStateOf(defaultValue) } }
    }
  }
}