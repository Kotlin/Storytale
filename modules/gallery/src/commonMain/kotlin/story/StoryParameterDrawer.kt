package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.generated.resources.Res
import org.jetbrains.compose.storytale.gallery.generated.resources.story_widget_icon
import org.jetbrains.compose.storytale.gallery.story.code.CodeBlock
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
) {
  Column(Modifier.padding(vertical = 28.dp)) {
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
    Column(Modifier.padding(horizontal = 20.dp)) {
      Column(Modifier.weight(1f)) {
        Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
          @Suppress("UNCHECKED_CAST")
          activeStory.parameters.forEach { parameter ->
              when (parameter.type) {
                 String::class ->
                     StringParameterField(
                         parameterName = parameter.name,
                         state = parameter.state as MutableState<String>,
                         modifier = Modifier.fillMaxWidth()
                     )
                  Boolean::class ->
                      BooleanParameterField(
                          parameterName = parameter.name,
                          state = parameter.state as MutableState<Boolean>,
                          modifier = Modifier.fillMaxWidth()
                      )
                  else ->
                      error("Unsupported parameter type ${parameter.type}")
              }
          }
        }
      }
      CodeBlock(
        code = """
      Column(Modifier.weight(1f)) {
        Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
          // fake data
          StringParameterField(
            parameterName = "Button Text",
            defaultString = "My Button",
            description = ""${'"'}
            controls the enabled state of this button. When false, 
            this component will not respond to user input, and it will appear visually disabled and disabled to accessibility services.
            ""${'"'}.trimIndent(),
            modifier = Modifier.fillMaxWidth()
          )
          BooleanParameterField(
            parameterName = "Enabled",
            defaultVale = true,
            description = ""${'"'}
            controls the enabled state of this button. When false, 
            this component will not respond to user input, and it will appear visually disabled and disabled to accessibility services.
            ""${'"'}.trimIndent(),
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
        """.trimIndent(),
      )
    }
  }
}
