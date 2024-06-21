package org.jetbrains.compose.storytale.gallery

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.compose.ScreenSize
import org.jetbrains.compose.storytale.gallery.compose.isMobile
import org.jetbrains.compose.storytale.gallery.story.StoryNavigationBar
import org.jetbrains.compose.storytale.gallery.story.StoryParameters
import org.jetbrains.compose.storytale.gallery.ui.component.HorizontalSplitPane
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun StoryGallery(stories: List<Story>) {
  var activeStoryIndex by remember { mutableIntStateOf(0) }
  val onSelectStory = { index: Int -> activeStoryIndex = index }
  val activeStory = stories[activeStoryIndex]

  when (!ScreenSize.isMobile) {
    true -> HorizontalSplitPane(
      minimumWidth = 320.dp,
      left = {
        StoryNavigationBar(activeStoryIndex, stories, onSelectStory)
      },
      content = {
        with(activeStory) { content() }
      },
      right = {
        StoryParameters(activeStory)
      }
    )
    false -> StoryNavigationBar(activeStoryIndex, stories, onSelectStory, Modifier.systemBarsPadding())
  }
}
