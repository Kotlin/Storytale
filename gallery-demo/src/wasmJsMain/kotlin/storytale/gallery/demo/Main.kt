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
import org.jetbrains.compose.storytale.gallery.story.code.JetBrainsMonoRegularRes
import org.jetbrains.compose.storytale.gallery.ui.theme.LocalCustomDensity
import org.jetbrains.compose.storytale.generated.MainViewController
import org.jetbrains.compose.storytale.storiesStorage
import org.jetbrains.storytale.gallery2.StoryScreen
import org.jetbrains.storytale.gallery2.Testing

@OptIn(ExperimentalResourceApi::class, ExperimentalBrowserHistoryApi::class)
fun main() {
    MainViewController() // Storytale compiler will initialize the stories

    ComposeViewport(viewportContainerId = "composeApplication") {
        val isReady = preloadFont(JetBrainsMonoRegularRes).value != null

        CompositionLocalProvider(
            LocalCustomDensity provides Density(LocalDensity.current.density * 0.8f),
        ) {
            if (isReady) {
                val navHostController = rememberNavController()

                Testing(navController = navHostController)

                LaunchedEffect(Unit) {
                    val initRoute = window.location.hash.substringAfter('#', "")
                    when {
                        initRoute.startsWith("story=") -> {
                            val storyName = initRoute.substringAfter("story=", "")
                            println("Navigate to $storyName")
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

