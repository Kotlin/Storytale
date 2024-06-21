package gallery

import StoryTextInput
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import storytale.modules.gallery.generated.resources.Res
import storytale.modules.gallery.generated.resources.compose_multiplatform
import ui.component.CenterRow
import ui.component.Gap

@Composable
fun StoryNavigationBar(
  story: List<String>,
  modifier: Modifier = Modifier
) = Column(
  modifier = Modifier.background(Color.White).then(modifier)
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
  StoryGalleryList(story)
}
