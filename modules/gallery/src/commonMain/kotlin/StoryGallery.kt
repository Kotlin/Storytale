import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.ScreenSize
import compose.isMobile
import gallery.StoryGalleryContent
import gallery.StoryNavigationBar
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.component.HorizontalSplitPane

@Composable
@Preview
fun StoryGallery(
  story: List<String> = (1..60).map { "MyButton" },
) = when (!ScreenSize.isMobile) {
  true -> HorizontalSplitPane(
    minimumWidth = 320.dp,
    left = {
      StoryNavigationBar(story)
    },
    right = {
      StoryGalleryContent()
    }
  )
  false -> StoryNavigationBar(story, Modifier.systemBarsPadding())
}
