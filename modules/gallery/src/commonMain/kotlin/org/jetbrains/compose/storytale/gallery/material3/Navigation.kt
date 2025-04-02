package org.jetbrains.compose.storytale.gallery.material3

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("story")
data class StoryScreen(val storyName: String)
