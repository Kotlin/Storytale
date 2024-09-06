import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.storytale.example.Res
import org.jetbrains.storytale.example.compose_multiplatform

@Composable
fun ComposeLogo(size: LogoSize = LogoSize.MEDIUM) {
  val widthAndHeight = when (size) {
    LogoSize.SMALL -> 300
    LogoSize.MEDIUM -> 600
    LogoSize.LARGE -> 900
  }

  Image(
    painter = painterResource(Res.drawable.compose_multiplatform),
    contentDescription =  "Compose Multiplatform Logo",
    modifier = Modifier.size(widthAndHeight.dp)
  )
}

enum class LogoSize {
  SMALL,
  MEDIUM,
  LARGE
}
