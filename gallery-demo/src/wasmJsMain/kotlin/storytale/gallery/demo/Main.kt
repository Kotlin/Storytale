package storytale.gallery.demo

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.compose.ui.window.ComposeViewport
import org.jetbrains.compose.storytale.gallery.Gallery

fun main() {
    ComposeViewport(viewportContainerId = "composeApplication") {
        App()
    }
}

@Composable
fun App() = Gallery()
