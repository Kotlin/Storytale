package org.jetbrains.compose.storytale.gallery.platform

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.story.StoryNavigationBar
import org.jetbrains.compose.storytale.gallery.story.StoryParameterDrawer
import org.jetbrains.compose.storytale.gallery.ui.component.HorizontalSplitPane

@Composable
actual fun StoryGallery(
  stories: List<Story>,
  modifier: Modifier
) {
  var activeStoryId by remember { mutableIntStateOf(-1) }
  val onSelectStory = { id: Int -> activeStoryId = id }
  val activeStory = stories.find { it.id == activeStoryId }
  HorizontalSplitPane {
    StoryNavigationBar(
      activeStoryId = activeStoryId,
      stories = stories,
      onSelectStory = onSelectStory,
      modifier = Modifier.widthIn(min = 350.dp)
    )
    Row(
      modifier = Modifier.fillMaxSize()
        .background(Color(0xFFFAFAFA))
    ) {
      if (activeStory != null) {
        Box(
          modifier = Modifier.weight(1f).fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          with(activeStory) {
            content()
          }
        }
        StoryParameterDrawer(
          activeStory = activeStory,
          modifier = Modifier
            .border(
              width = 1.dp,
              color = Color.Black.copy(.11f),
              shape = RoundedCornerShape(topStart = 36.dp, bottomStart = 36.dp)
            )
            .clip(RoundedCornerShape(topStart = 36.dp, bottomStart = 36.dp))
            .background(Color.White)
        )
      }
    }
  }
}