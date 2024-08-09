package org.jetbrains.compose.storytale.gallery.platform.mobile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.compose.clickableWithoutRipple
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
        1 -> CodeBlock(
          """
            @Composable
            fun MobileStoryParameterSheet(
              sheetState: StoryBottomSheetState,
              story: Story
            ) {
              val pagerState = rememberPagerState(pageCount = 2)
              StoryBottomSheet(
                sheetState = sheetState,
                onDismissRequest = sheetState::hide,
                containerColor = Color.White,
                modifier = Modifier.statusBarsPadding()
                  .padding(top = 24.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
              ) {
                HorizontalPager(
                  state = pagerState
                ) {
                  when (it) {
                    0 -> StoryParameter(
                      activeStory = story,
                      showStoryName = false
                    )
                    1 -> CodeBlock(

                    )
                  }
                }
              }
            }
          """.trimIndent(),
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}
