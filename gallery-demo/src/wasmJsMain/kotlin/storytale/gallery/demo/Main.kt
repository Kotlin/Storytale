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

    val initRoute = window.location.hash.substringAfter('#', "")
    val storyName = initRoute.substringAfter("story=", "")
    val useEmbedded = window.location.search.contains("embedded=true")

    ComposeViewport(viewportContainerId = "composeApplication") {
        val hasResourcePreloadCompleted = preloadFont(JetBrainsMonoRegularRes).value != null

        CompositionLocalProvider(
            LocalCustomDensity provides Density(LocalDensity.current.density * 0.75f),
        ) {
            if (hasResourcePreloadCompleted) {
                if (useEmbedded) {
                    EmbeddedStoryView(
                        activeStory = storiesStorage.firstOrNull {
                            it.name == storyName
                        } ?: storiesStorage.first()
                    )
                } else {
                    val navHostController = rememberNavController()
                    FullStorytaleGallery(
                        navController = navHostController
                    )

                    LaunchedEffect(Unit) {
                        when {
                            initRoute.startsWith("story=") -> {
                                navHostController.navigate(StoryScreen(storyName))
                            }
                        }
                        window.bindToNavigation(navHostController) { entry ->
                            val route = entry.destination.route.orEmpty()
                            when {
                                route.startsWith(StoryScreen.serializer().descriptor.serialName) -> {
                                    val storyName = entry.toRoute<StoryScreen>().storyName
                                    "#story=$storyName"
                                }
                                else -> ""
                            }
                        }
                    }
                }
            }
        }
    }
}

