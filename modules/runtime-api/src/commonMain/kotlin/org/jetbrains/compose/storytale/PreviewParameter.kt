package org.jetbrains.compose.storytale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
inline fun <reified T> previewParameter(defaultValue: T) = LocalStory.current.parameter(defaultValue)

@Composable
inline fun <reified T> previewParameter(
    values: List<T>,
    defaultValueIndex: Int = 0,
    label: String? = null,
) = LocalStory.current.parameter(values, defaultValueIndex, label)

@Composable
inline fun <reified T : Enum<T>> previewParameter(defaultValue: T, label: String? = null) = LocalStory.current.parameter(defaultValue, label)

@PublishedApi
internal val LocalStory = staticCompositionLocalOf { Story(-1, "DefaultPreviewStory", "", "", {}) }
