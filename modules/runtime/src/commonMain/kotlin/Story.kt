import androidx.compose.runtime.Composable

abstract class Story {
  abstract val storyName: String
  @Composable
  abstract fun content()
}
