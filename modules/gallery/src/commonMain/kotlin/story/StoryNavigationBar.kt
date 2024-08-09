package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.generated.resources.Res
import org.jetbrains.compose.storytale.gallery.generated.resources.compose_multiplatform
import org.jetbrains.compose.storytale.gallery.platform.StoryEmptyStatus
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap

@Composable
fun StoryNavigationBar(
  activeStoryId: Int,
  stories: List<Story>,
  onSelectStory: (Int) -> Unit,
  modifier: Modifier = Modifier
) = Column(
  modifier = Modifier.fillMaxHeight()
    .background(Color.White)
    .then(modifier)
) {
  var searchQuery by remember { mutableStateOf("") }
  Column(
    modifier = Modifier.padding(20.dp)
  ) {
    CenterRow {
      Image(
        painter = painterResource(Res.drawable.compose_multiplatform),
        contentDescription = null,
        modifier = Modifier.size(36.dp)
      )
      Gap(14.dp)
      Text(
        text = "Storytale",
        fontWeight = FontWeight.Medium,
        color = Color(0xFF1E1E1E),
        fontSize = 24.sp
      )
    }
    if (stories.isNotEmpty()) {
      Gap(18.dp)
      StorySearchBar(
        text = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
  AnimatedContent(
    targetState = searchQuery.isEmpty(),
    transitionSpec = {
      ContentTransform(
        targetContentEnter = fadeIn(),
        initialContentExit = fadeOut(),
        sizeTransform = null
      )
    },
    modifier = Modifier.fillMaxHeight()
  ) {
    when (it) {
      true -> {
        when (stories.isEmpty()) {
          true -> StoryEmptyStatus()
          false -> StoryList(activeStoryId, stories, onSelectStory)
        }
      }
      else -> StorySearchList(
        result = stories.filter { story ->
          story.name.contains(searchQuery, ignoreCase = true)
        },
        activeStoryIndex = activeStoryId,
        onSelectStory = onSelectStory
      )
    }
  }
}
