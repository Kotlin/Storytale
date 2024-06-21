package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.generated.resources.Res
import org.jetbrains.compose.storytale.gallery.generated.resources.story_widget_icon
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import org.jetbrains.compose.storytale.gallery.ui.theme.currentColorScheme

@Composable
fun StoryParameters(activeStory: Story) = Box(
  modifier = Modifier.fillMaxSize()
    .background(Color(0xFFF8F9FD))
) {
  StoryGalleryParameterDrawer(activeStory, Modifier.align(Alignment.CenterEnd))
}

@Composable
fun StoryGalleryParameterDrawer(
  activeStory: Story,
  modifier: Modifier = Modifier
) = Box(
  modifier = modifier.fillMaxHeight()
    .widthIn(max = 280.dp)
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
        text = activeStory.name,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
      )
    }
    Gap(36.dp)
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
