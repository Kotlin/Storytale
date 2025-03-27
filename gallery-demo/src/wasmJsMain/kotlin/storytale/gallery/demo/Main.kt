package storytale.gallery.demo

import androidx.compose.ui.window.ComposeViewport
import org.jetbrains.compose.storytale.generated.MainViewController
import org.jetbrains.storytale.gallery2.Testing

fun main() {
    MainViewController() // Storytale compiler will initialize the stories

    ComposeViewport(viewportContainerId = "composeApplication") {
        Testing()
    }
}

