package storytale.gallery.demo

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToNavigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.browser.window
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadFont
import org.jetbrains.compose.storytale.gallery.material3.EmbeddedStoryView
import org.jetbrains.compose.storytale.gallery.material3.FullStorytaleGallery
import org.jetbrains.compose.storytale.gallery.material3.StoryScreen
import org.jetbrains.compose.storytale.gallery.story.code.JetBrainsMonoRegularRes
import org.jetbrains.compose.storytale.gallery.ui.theme.LocalCustomDensity
import org.jetbrains.compose.storytale.generated.MainViewController
import org.jetbrains.compose.storytale.storiesStorage

@OptIn(ExperimentalResourceApi::class, ExperimentalBrowserHistoryApi::class)
fun main() {
    MainViewController() // Storytale compiler will initialize the stories

    val useEmbedded = window.location.search.contains("embedded=true")

    ComposeViewport(viewportContainerId = "composeApplication") {
        val hasResourcePreloadCompleted = preloadFont(JetBrainsMonoRegularRes).value != null
        val navHostController = rememberNavController()

        CompositionLocalProvider(
            LocalCustomDensity provides Density(LocalDensity.current.density * 0.8f),
        ) {
            if (hasResourcePreloadCompleted) {
                if (useEmbedded) {
                    EmbeddedStoryView(navHostController = navHostController)
                } else {
                    FullStorytaleGallery(navController = navHostController)
                }

                LaunchedEffect(Unit) {
                    window.bindToNavigation(navHostController)
                }
            }
        }
    }
}

