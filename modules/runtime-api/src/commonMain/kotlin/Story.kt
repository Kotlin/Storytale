package org.jetbrains.compose.storytale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

val storiesStorage = mutableListOf<Story>()

inline fun story(code: String = "", crossinline content: @Composable Story.() -> Unit) = StoryDelegate({ content() }, code)

class StoryDelegate(
  private val content: @Composable Story.() -> Unit,
  private val code: String = ""
) {
  private lateinit var instance: Story

  operator fun getValue(thisRef: Any?, property: KProperty<*>): Story {
    return instance
  }

  operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): StoryDelegate {
    instance = Story(storiesStorage.size, property.name, code, content).also(storiesStorage::add)
    return this
  }
}

data class Story(
  val id: Int,
  val name: String,
  val code: String,
  val content: @Composable Story.() -> Unit
) {
  @PublishedApi
  internal val nameToParameterMapping = hashMapOf<String, StoryParameter<*>>()
  val parameters inline get() = nameToParameterMapping.values.toList()

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
    story.nameToParameterMapping.getValue(property.name).state.value as T

  @Composable
  operator fun provideDelegate(thisRef: Any?, property: KProperty<*>) = also {
    story.nameToParameterMapping.getOrPut(property.name) {
      StoryParameter(property.name, type) { remember { mutableStateOf(defaultValue) } }
    }
  }
}
