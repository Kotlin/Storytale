package org.jetbrains.compose.storytale.gallery.platform

import MobileGallery
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.navigation.Route
import org.jetbrains.compose.storytale.gallery.story.StoryNavigationBar

private const val defaultSlideAnimationTween = 350

@Composable
actual fun StoryGallery(
  stories: List<Story>,
  modifier: Modifier
) {
  val navController = rememberNavController()
  NavHost(
    navController = navController,
    startDestination = Route.MobileHome,
    modifier = modifier,
    enterTransition = { slideIntoContainer(Start, tween(defaultSlideAnimationTween)) },
    exitTransition = { slideOutOfContainer(End, tween(defaultSlideAnimationTween)) }
  ) {
    composable<Route.MobileHome>(
      enterTransition = { slideIntoContainer(End, tween(defaultSlideAnimationTween)) },
      exitTransition = { slideOutOfContainer(Start, tween(defaultSlideAnimationTween)) }
    ) {
      StoryNavigationBar(
        stories = stories,
        onSelectStory = { storyId ->
          navController.navigate(Route.MobileGallery(storyId))
        },
        activeStoryId = -1
      )
    }
    composable<Route.MobileGallery> { backStackEntry ->
      val storyId = backStackEntry.toRoute<Route.MobileGallery>().storyId
      Column {
        MobileGallery(
          story = stories.find { it.id == storyId },
          back = dropUnlessResumed {
            navController.popBackStack()
          }
        )
      }
    }
  }
}
