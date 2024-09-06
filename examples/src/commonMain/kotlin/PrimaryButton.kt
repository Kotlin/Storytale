import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun PrimaryButton(
  text: String,
  onClick: () -> Unit = {},
  enabled: Boolean = true
) {
  Button(onClick = onClick, enabled = enabled) {
    Text(text)
  }
}
