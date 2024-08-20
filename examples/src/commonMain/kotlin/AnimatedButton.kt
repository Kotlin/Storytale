import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.storytale.example.Res
import org.jetbrains.storytale.example.compose_multiplatform

@Composable
fun AnimatedButton() {
  var showContent by remember { mutableStateOf(false) }
  Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
    PrimaryButton(text = "Click Me", onClick = { showContent = !showContent })
    AnimatedVisibility(showContent) {
      val greeting = remember { Greeting().greet() }
      Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painterResource(Res.drawable.compose_multiplatform), null)
        Text("Compose: $greeting")
      }
    }
  }
}

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, enabled: Boolean = true) {
  Button(onClick = onClick, enabled = enabled) {
    Text(text)
  }
}