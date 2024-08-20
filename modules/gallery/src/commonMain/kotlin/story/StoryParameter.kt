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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
        modifier = Modifier.padding(top = 24.dp),
        thickness = 0.5.dp,
        color = Color.Black.copy(alpha = 0.4f)
      )
    }
    Column(
      modifier = Modifier.verticalScroll(rememberScrollState())
        .padding(
          start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
          end = contentPadding.calculateEndPadding(LocalLayoutDirection.current),
          top = if (showStoryName) 24.dp else 0.dp,
          bottom = contentPadding.calculateBottomPadding()
        )
    ) {
      Column(
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
  }
}
