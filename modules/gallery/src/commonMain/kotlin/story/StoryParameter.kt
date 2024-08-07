package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
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
fun StoryParameter(
  activeStory: Story,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(0.dp),
  showStoryName: Boolean = true
) = Box(modifier = modifier.fillMaxHeight()) {
  Column(
    modifier = Modifier.fillMaxWidth()
      .padding(
        top = contentPadding.calculateTopPadding(),
        bottom = contentPadding.calculateBottomPadding()
      )
  ) {
    if (showStoryName) {
      CenterRow(Modifier.padding(horizontal = 20.dp)) {
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
      HorizontalDivider(
        modifier = Modifier.padding(vertical = 24.dp),
        thickness = 0.5.dp,
        color = Color.Black.copy(alpha = 0.4f)
      )
    }
    Column(
      modifier = Modifier.padding(
        start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
        end = contentPadding.calculateEndPadding(LocalLayoutDirection.current)
      )
    ) {
      Column(Modifier.weight(1f)) {
        Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
          // fake data
          StringParameterField(
            parameterName = "Button Text",
            defaultString = "My Button",
            description = """
            controls the enabled state of this button. When false, 
            this component will not respond to user input, and it will appear visually disabled and disabled to accessibility services.
            """.trimIndent(),
            modifier = Modifier.fillMaxWidth()
          )
          BooleanParameterField(
            parameterName = "Enabled",
            defaultVale = true,
            description = """
            controls the enabled state of this button. When false, 
            this component will not respond to user input, and it will appear visually disabled and disabled to accessibility services.
            """.trimIndent(),
            modifier = Modifier.fillMaxWidth()
          )

          BooleanParameterField(
            parameterName = "Enabled",
            defaultVale = true,
            description = """
            controls the enabled state of this button. When false, 
            this component will not respond to user input, and it will appear visually disabled and disabled to accessibility services.
            """.trimIndent(),
            modifier = Modifier.fillMaxWidth()
          )

          BooleanParameterField(
            parameterName = "Enabled",
            defaultVale = true,
            description = """
            controls the enabled state of this button. When false, 
            this component will not respond to user input, and it will appear visually disabled and disabled to accessibility services.
            """.trimIndent(),
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }
  }
}
