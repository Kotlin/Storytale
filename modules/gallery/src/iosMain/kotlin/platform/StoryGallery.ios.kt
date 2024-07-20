package org.jetbrains.compose.storytale.gallery.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.navigation.Route
import org.jetbrains.compose.storytale.gallery.story.StoryNavigationBar

@Composable
actual fun StoryGallery(
  stories: List<Story>,
  modifier: Modifier
) {
  val navController = rememberNavController()
  var activeStoryId by remember { mutableIntStateOf(-1) }
  NavHost(
    navController = navController,
    startDestination = Route.MobileGallery
  ) {
    composable<Route.MobileHome> {
      StoryNavigationBar(
        stories = stories,
        onSelectStory = {
          activeStoryId = it
          navController.navigate(Route.MobileGallery(activeStoryId))
        },
        activeStoryId = activeStoryId
      )
    }
    composable<Route.MobileGallery> { backStackEntry ->
      val parameter = backStackEntry.toRoute<Route.MobileGallery>()
      stories.find { it.id == parameter.storyId }?.let { story ->
        with(story) {
          content()
        }
      }
    }
  }
}
