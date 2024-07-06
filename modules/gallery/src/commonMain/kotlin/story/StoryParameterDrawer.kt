package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import org.jetbrains.compose.storytale.gallery.story.parameters.BooleanParameterField
import org.jetbrains.compose.storytale.gallery.story.parameters.StringParameterField
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import org.jetbrains.compose.storytale.gallery.ui.theme.currentColorScheme

@Composable
fun StoryParameterDrawer(
  activeStory: Story,
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
        text = activeStory.name,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
      )
    }
    Gap(36.dp)
    Column(
      verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
      // fake data
      StringParameterField(
        parameterName = "Button Text",
        defaultString = "My Button",
        modifier = Modifier.fillMaxWidth()
      )
      BooleanParameterField(
        parameterName = "Enabled",
        defaultVale = true,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}
