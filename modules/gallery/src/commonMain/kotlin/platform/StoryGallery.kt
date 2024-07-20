package org.jetbrains.compose.storytale.gallery.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.storytale.Story

@Composable
expect fun StoryGallery(
  stories: List<Story>,
  modifier: Modifier = Modifier
)