package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.StoryTextInput
import org.jetbrains.compose.storytale.gallery.generated.resources.Res
import org.jetbrains.compose.storytale.gallery.generated.resources.compose_multiplatform
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap

@Composable
fun StoryNavigationBar(
  activeStoryIndex: Int,
  stories: List<Story>,
  onSelectStory: (Int) -> Unit,
  modifier: Modifier = Modifier
) = Column(
  modifier = Modifier.fillMaxHeight().background(Color.White).then(modifier)
) {
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
    Gap(18.dp)
    StoryTextInput(Modifier.fillMaxWidth())
  }
  StoryGalleryList(activeStoryIndex, stories, onSelectStory)
}
