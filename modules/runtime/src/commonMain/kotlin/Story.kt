import androidx.compose.runtime.Composable

data class Story(
  val name: String,
  val content: @Composable () -> Unit
)