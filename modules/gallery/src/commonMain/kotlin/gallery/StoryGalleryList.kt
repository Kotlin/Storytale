package gallery

import StoryComponentListItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.component.CenterRow
import ui.component.Gap
import ui.component.NumberChip

@Composable
fun StoryGalleryList(
  story: List<String>
) {
  var currentStory by remember { mutableIntStateOf(0) }
  LazyColumn(
    contentPadding = PaddingValues(bottom = 20.dp)
  ) {
    item {
      CenterRow(Modifier.padding(horizontal = 20.dp)) {
        Text(
          text = "Story Component",
          fontWeight = FontWeight.SemiBold,
          color = Color.Black,
          fontSize = 20.sp,
          modifier = Modifier.weight(1f)
        )
        NumberChip(25)
      }
      Gap(14.dp)
      Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        story.forEachIndexed { index, s ->
          StoryComponentListItem(
            selected = index == currentStory,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
          ) {
            currentStory = index
          }
        }
      }
    }
  }
}
