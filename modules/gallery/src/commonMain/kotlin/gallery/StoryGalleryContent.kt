package gallery
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import storytale.modules.gallery.generated.resources.Res
import storytale.modules.gallery.generated.resources.story_widget_icon
import ui.component.CenterRow
import ui.component.Gap
import ui.theme.currentColorScheme

@Composable
fun StoryGalleryContent() = Box(
  modifier = Modifier.fillMaxSize()
    .background(Color(0xFFF8F9FD))
) {
  StoryGalleryParameterDrawer(Modifier.align(Alignment.CenterEnd))
}

@Composable
fun StoryGalleryParameterDrawer(
  modifier: Modifier = Modifier
) = Box(
  modifier = modifier.fillMaxHeight()
    .widthIn(max = 280.dp)
    .clip(RoundedCornerShape(topStart = 36.dp, bottomStart = 36.dp))
    .background(Color.White)
) {
  Column(Modifier.padding(horizontal = 24.dp, vertical = 28.dp)) {
    CenterRow {
      Icon(
        painter = painterResource(Res.drawable.story_widget_icon),
        contentDescription = null,
        modifier = Modifier.size(24.dp),
        tint = currentColorScheme.primaryText,
      )
      Gap(11.dp)
      Text(
        text = "MyButton",
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
      )
    }
    Gap(36.dp)
    StoryGalleryParameterItem()
  }
}

@Composable
fun StoryGalleryParameterItem() = Column(
  verticalArrangement = Arrangement.spacedBy(12.dp),
) {
  Text(
    text = "Button Text",
    fontSize = 17.sp,
    fontWeight = FontWeight.Medium,
    color = currentColorScheme.primaryText.copy(.67f),
  )
  ParameterTextField(Modifier.fillMaxWidth())
}
