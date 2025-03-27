package storytale.gallery.demo

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.window.ComposeViewport
import org.jetbrains.compose.storytale.gallery.ui.theme.LocalCustomDensity
import org.jetbrains.compose.storytale.generated.MainViewController
import org.jetbrains.storytale.gallery2.Testing

fun main() {
    MainViewController() // Storytale compiler will initialize the stories

    ComposeViewport(viewportContainerId = "composeApplication") {
        CompositionLocalProvider(
            LocalCustomDensity provides Density(LocalDensity.current.density * 0.8f),
        ) {
            Testing()
        }
    }
}

