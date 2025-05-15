package storytale.gallery.demo

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToNavigation
import androidx.navigation.compose.rememberNavController
import kotlinx.browser.window
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadFont
import org.jetbrains.compose.storytale.gallery.material3.StorytaleGalleryApp
import org.jetbrains.compose.storytale.gallery.material3.openFullScreenStory
import org.jetbrains.compose.storytale.gallery.story.code.JetBrainsMonoRegularRes
import org.jetbrains.compose.storytale.generated.MainViewController

@OptIn(ExperimentalResourceApi::class, ExperimentalBrowserHistoryApi::class)
fun main() {
    MainViewController() // Storytale compiler will initialize the stories

    openFullScreenStory = { story, _ ->
        window.open(window.location.origin + "/#story/" + story.storyName)
    }

    val useEmbedded = window.location.search.contains("embedded=true")

    ComposeViewport(viewportContainerId = "composeApplication") {
        val hasResourcePreloadCompleted = preloadFont(JetBrainsMonoRegularRes).value != null
        val navHostController = rememberNavController()

        if (hasResourcePreloadCompleted) {
            StorytaleGalleryApp(isEmbedded = useEmbedded, navHostController)

            LaunchedEffect(Unit) {
                window.bindToNavigation(navHostController)
            }
        }
    }
}
