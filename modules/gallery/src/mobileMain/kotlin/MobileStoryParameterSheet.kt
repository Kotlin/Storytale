import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.compose.clickableWithoutRipple
import org.jetbrains.compose.storytale.gallery.generated.resources.Res
import org.jetbrains.compose.storytale.gallery.generated.resources.check
import org.jetbrains.compose.storytale.gallery.generated.resources.copy
import org.jetbrains.compose.storytale.gallery.story.StoryParameter
import org.jetbrains.compose.storytale.gallery.story.code.CodeBlock
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import org.jetbrains.compose.storytale.gallery.ui.component.sheet.StoryBottomSheet
import org.jetbrains.compose.storytale.gallery.ui.component.sheet.StoryBottomSheetState

private enum class StoryPage {
  Properties,
  SourceCode
}

@Composable
fun MobileStoryParameterSheet(
  sheetState: StoryBottomSheetState,
  story: Story
) {
  val pagerState = rememberPagerState { 2 }
  val scope = rememberCoroutineScope()
  StoryBottomSheet(
    sheetState = sheetState,
    onDismissRequest = sheetState::hide,
    containerColor = Color.White,
    modifier = Modifier.statusBarsPadding().padding(top = 24.dp),
    contentWindowInsets = { WindowInsets.displayCutout }
  ) {
    CenterRow(
      modifier = Modifier.padding(horizontal = 20.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      StoryPage.entries.forEachIndexed { index, storyPage ->
        val isSelected = index == pagerState.currentPage
        val style = when (isSelected) {
          true -> TextStyle(
            color = Color(0xFF0021CE),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
          )
          false -> TextStyle(
            color = Color(0xFFAAAAAA),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
          )
        }
        Text(
          text = storyPage.name,
          style = style,
          modifier = Modifier.clickableWithoutRipple {
            scope.launch {
              pagerState.animateScrollToPage(index)
            }
          }
        )
      }
    }
    Gap(24.dp)
    HorizontalPager(
      state = pagerState,
      modifier = Modifier.fillMaxSize()
    ) {
      when (it) {
        0 -> StoryParameter(
          activeStory = story,
          showStoryName = false,
          contentPadding = PaddingValues(horizontal = 20.dp)
        )
        1 -> MobileCodeBlock(
          code = story.code,
        )
      }
    }
  }
}

@Composable
private fun MobileCodeBlock(
  code: String,
  modifier: Modifier = Modifier
) = Box(modifier = modifier) {
  val clipboard = LocalClipboardManager.current
  val scope = rememberCoroutineScope()
  var copied by remember { mutableStateOf(false) }
  CodeBlock(
    code = code,
    modifier = Modifier.fillMaxSize()
  )
  Box(
    modifier = Modifier.align(Alignment.BottomEnd)
      .padding(end = 24.dp)
      .navigationBarsPadding()
      .clip(RoundedCornerShape(10.dp))
      .background(Color(0xFF5448D9))
      .clickable {
        scope.launch {
          copied = true
          delay(1000)
          copied = false
        }
        clipboard.setText(AnnotatedString(code))
      }
  ) {
    AnimatedContent(
      targetState = copied,
      transitionSpec = {
        ContentTransform(
          targetContentEnter = fadeIn() + slideIntoContainer(Up),
          initialContentExit = fadeOut() + slideOutOfContainer(Down),
          sizeTransform = SizeTransform()
        )
      }
    ) {
      when (it) {
        true -> CenterRow(modifier = Modifier.padding(8.dp)) {
          Icon(
            painter = painterResource(Res.drawable.check),
            contentDescription = "Copy code",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
          Gap(6.dp)
          Text(
            text = "Copied to clipboard!",
            fontWeight = FontWeight.SemiBold,
            color = Color.White
          )
        }
        false -> Icon(
          painter = painterResource(Res.drawable.copy),
          contentDescription = "Copy code",
          tint = Color.White,
          modifier = Modifier.padding(8.dp)
        )
      }
    }
  }
}
