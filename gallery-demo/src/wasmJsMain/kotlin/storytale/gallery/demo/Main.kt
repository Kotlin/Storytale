package storytale.gallery.demo

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.window.ComposeViewport
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadFont
import org.jetbrains.compose.storytale.gallery.story.code.JetBrainsMonoRegularRes
import org.jetbrains.compose.storytale.gallery.ui.theme.LocalCustomDensity
import org.jetbrains.compose.storytale.generated.MainViewController
import org.jetbrains.storytale.gallery2.Testing

@OptIn(ExperimentalResourceApi::class)
fun main() {
    MainViewController() // Storytale compiler will initialize the stories

    ComposeViewport(viewportContainerId = "composeApplication") {
        val isReady = preloadFont(JetBrainsMonoRegularRes).value != null

        CompositionLocalProvider(
            LocalCustomDensity provides Density(LocalDensity.current.density * 0.8f),
        ) {
            if (isReady) {
                Testing()
            }
        }
    }
}

