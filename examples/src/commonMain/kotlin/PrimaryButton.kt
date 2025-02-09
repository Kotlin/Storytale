import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PrimaryButton(
  text: String,
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {},
  enabled: Boolean = true,
) {
  Button(
    modifier = modifier,
    onClick = onClick,
    enabled = enabled,
  ) {
    Text(text)
  }
}
