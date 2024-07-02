package org.jetbrains.compose.storytale.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.compose.ScreenSize
import org.jetbrains.compose.storytale.gallery.compose.isMobile
import org.jetbrains.compose.storytale.gallery.story.StoryGalleryParameterDrawer
import org.jetbrains.compose.storytale.gallery.story.StoryNavigationBar
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
      right = {
        Row(
          modifier = Modifier.fillMaxSize()
            .background(Color(0xFFFAFAFA))
        ) {
          Box(
            modifier = Modifier
              .weight(1f)
              .fillMaxSize(),
            contentAlignment = Alignment.Center
          ) {
            with(activeStory) { content() }
          }
          StoryGalleryParameterDrawer(activeStory)
        }
      }
    )
    false -> StoryNavigationBar(activeStoryIndex, stories, onSelectStory, Modifier.systemBarsPadding())
  }
}
