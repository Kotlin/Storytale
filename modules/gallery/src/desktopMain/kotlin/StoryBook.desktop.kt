import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

actual fun getStoryBook(): List<Story> = buildStoryBook {
  addStory(
    object : Story() {
      override val storyName: String = "Desktop Button"
      @Composable
      override fun content() {
        Button(onClick = {}) {
          Text("Desktop Button")
        }
      }
    }
  )
}
