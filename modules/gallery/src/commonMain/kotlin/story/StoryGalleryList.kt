package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.StoryComponentListItem
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import org.jetbrains.compose.storytale.gallery.ui.component.NumberChip

@Composable
fun StoryGalleryList(
  activeStoryIndex: Int,
  stories: List<Story>,
  onSelectStory: (index: Int) -> Unit
) {
  LazyColumn(
    contentPadding = PaddingValues(bottom = 20.dp)
  ) {
    item {
      CenterRow(Modifier.padding(horizontal = 20.dp)) {
        Text(
          text = "Stories",
          fontWeight = FontWeight.SemiBold,
          color = Color.Black,
          fontSize = 20.sp,
          modifier = Modifier.weight(1f)
        )
        NumberChip(stories.size)
      }
      Gap(14.dp)
      Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        stories.forEachIndexed { index, s ->
          StoryComponentListItem(
            story = s,
            selected = index == activeStoryIndex,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            onClick = { onSelectStory(index) }
          )
        }
      }
    }
  }
}
