import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ui.theme.StoryGalleryTheme

fun main() = application {
  Window(
    onCloseRequest = ::exitApplication,
    title = "Storytale",
    state = rememberWindowState(
      width = 1290.dp,
      height = 720.dp
    )
  ) {
    StoryGalleryTheme {
      StoryGallery()
    }
  }
}
