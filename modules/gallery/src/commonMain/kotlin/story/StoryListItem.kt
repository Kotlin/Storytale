package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
fun StoryListItem(
  story: Story,
  selected: Boolean,
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(12.dp),
  onClick: () -> Unit,
) {
  val animatedBackgroundColor by animateColorAsState(
    targetValue = when (selected) {
      true -> currentColorScheme.primaryText
      false -> Color.Transparent
    }
  )
  val animatedIconColor by animateColorAsState(
    targetValue = when (selected) {
      true -> Color.White
      else -> currentColorScheme.primaryText
    }
  )
  val animatedTextColor by animateColorAsState(
    targetValue = when (selected) {
      true -> Color.White
      else -> currentColorScheme.primaryText
    }
  )
  Box(
    modifier = modifier
      .clip(shape)
      .background(
        color = animatedBackgroundColor,
        shape = shape
      )
      .clickable { onClick() }
  ) {
    CenterRow(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
      Icon(
        painter = painterResource(Res.drawable.story_widget_icon),
        contentDescription = null,
        modifier = Modifier.size(20.dp),
        tint = animatedIconColor,
      )
      Gap(11.dp)
      Text(
        text = story.name,
        color = animatedTextColor,
        fontSize = 15.sp
      )
    }
  }
}