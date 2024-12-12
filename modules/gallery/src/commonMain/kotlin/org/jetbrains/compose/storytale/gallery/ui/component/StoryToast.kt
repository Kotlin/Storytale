package org.jetbrains.compose.storytale.gallery.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.storytale.gallery.compose.LaunchedValueEffect
import org.jetbrains.compose.storytale.gallery.generated.resources.Res
import org.jetbrains.compose.storytale.gallery.generated.resources.story_widget_icon
import org.jetbrains.compose.storytale.gallery.ui.theme.currentColorScheme

@Stable
class StoryToastState {
  private var previous: String = ""

  var toastMessage by mutableStateOf("")
  var toastDuration by mutableLongStateOf(2500)

  val isSwitching = previous.isNotEmpty() && previous != toastMessage && toastMessage.isNotEmpty()

  fun show(message: String) {
    previous = toastMessage
    toastMessage = message
  }

  fun dismiss() {
    toastMessage = ""
  }
}

@Composable
fun rememberStoryToastState(): StoryToastState = remember { StoryToastState() }

@Composable
fun StoryToast(
  toastState: StoryToastState,
  modifier: Modifier = Modifier,
) {
  val isSwitching = toastState.isSwitching
  AnimatedContent(
    targetState = toastState.toastMessage,
    label = "toast",
    transitionSpec = {
      ContentTransform(
        targetContentEnter = slideInVertically { it } + fadeIn(tween(if (isSwitching) 300 else 400)),
        initialContentExit = slideOutVertically(tween(260), targetOffsetY = { it }) + fadeOut(),
        sizeTransform = null
      )
    },
    modifier = modifier.padding(horizontal = 70.dp)
  ) { message ->
    if (message.isNotEmpty()) {
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(currentColorScheme.primaryText),
        contentAlignment = Alignment.Center
      ) {
        CenterRow(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
          Icon(
            painter = painterResource(Res.drawable.story_widget_icon),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = Color.White
          )
          Gap(6.dp)
          Text(
            text = message,
            fontSize = 16.sp,
            color = Color.White
          )
        }
      }
    }
  }
  LaunchedValueEffect(toastState.toastMessage) {
    if (it.isNotEmpty()) {
      delay(toastState.toastDuration)
      toastState.dismiss()
    }
  }
}
