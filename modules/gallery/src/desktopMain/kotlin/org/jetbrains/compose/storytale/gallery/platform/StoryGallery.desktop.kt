package org.jetbrains.compose.storytale.gallery.platform

import org.jetbrains.compose.storytale.gallery.DesktopCodeBlock
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.event.Event
import org.jetbrains.compose.storytale.gallery.event.EventCenter
import org.jetbrains.compose.storytale.gallery.story.StoryNavigationBar
import org.jetbrains.compose.storytale.gallery.story.StoryParameter
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.HorizontalSplitPane
import org.jetbrains.compose.storytale.gallery.ui.component.StoryToast
import org.jetbrains.compose.storytale.gallery.ui.component.SwitchButton
import org.jetbrains.compose.storytale.gallery.ui.component.rememberStoryToastState
import org.jetbrains.compose.storytale.gallery.ui.theme.currentColorScheme

@Composable
actual fun StoryGallery(
  stories: List<Story>,
  modifier: Modifier
) = HorizontalSplitPane(
  modifier = modifier,
  initialFirstPlaceableWith = 350.dp
) {
  val toast = rememberStoryToastState()
  var activeStoryId by remember { mutableIntStateOf(-1) }
  var sourceCodeMode by remember(activeStoryId) { mutableStateOf(false) }

  val cornerAnimation by animateDpAsState(
    targetValue = if (sourceCodeMode) 0.dp else 36.dp
  )
  val widthAnimation by animateDpAsState(
    targetValue = if (sourceCodeMode) 450.dp else 280.dp
  )

  val onSelectStory = { id: Int -> activeStoryId = id }
  val activeStory = stories.find { it.id == activeStoryId }

  StoryNavigationBar(
    activeStoryId = activeStoryId,
    stories = stories,
    onSelectStory = onSelectStory
  )

  Row(
    modifier = Modifier.fillMaxSize()
      .background(Color(0xFFFAFAFA))
  ) {
    if (activeStory != null) {
      Box(
        modifier = Modifier.weight(1f).fillMaxSize()
      ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          with(activeStory) {
            content()
          }
        }
        StoryToast(
          toastState = toast,
          modifier = Modifier.align(Alignment.BottomCenter)
            .padding(bottom = 24.dp)
        )
      }
      Column(
        modifier = Modifier.widthIn(max = widthAnimation)
          .border(
            width = 1.dp,
            color = Color.Black.copy(.11f),
            shape = RoundedCornerShape(topStart = cornerAnimation, bottomStart = cornerAnimation)
          )
          .clip(RoundedCornerShape(topStart = cornerAnimation, bottomStart = cornerAnimation))
          .background(Color.White),
      ) {
        AnimatedContent(
          targetState = sourceCodeMode,
          transitionSpec = {
            slideIntoContainer(Start) togetherWith slideOutOfContainer(Start)
          },
          modifier = Modifier.weight(1f)
        ) {
          when (it) {
            true -> DesktopCodeBlock(
              code = activeStory.code,
              storyName = activeStory.name,
              modifier = Modifier.fillMaxSize()
            )
            false -> StoryParameter(
              activeStory = activeStory,
              contentPadding = PaddingValues(horizontal = 20.dp, vertical = 28.dp)
            )
          }
        }
        CenterRow(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
          Text(
            text = "Source code",
            color = currentColorScheme.primaryText,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
          )
          SwitchButton(
            checked = sourceCodeMode,
            onValueChange = { sourceCodeMode = it }
          )
        }
      }
    }
  }
  LaunchedEffect(Unit) {
    EventCenter.observe<Event.CopyCode> {
      toast.show("Code copied to clipboard! ✨")
    }
  }
}
