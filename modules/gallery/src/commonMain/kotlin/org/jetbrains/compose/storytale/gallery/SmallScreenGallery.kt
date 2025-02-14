package org.jetbrains.compose.storytale.gallery

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.navigation.Route
import org.jetbrains.compose.storytale.gallery.story.StoryNavigationBar

private const val DEFAULT_SLIDE_ANIMATION_TWEEN = 350

@Composable
fun SmallScreenGallery(
  stories: List<Story>,
  navController: NavHostController,
  modifier: Modifier = Modifier,
) {
  NavHost(
    navController = navController,
    startDestination = Route.MobileHome,
    modifier = modifier,
    enterTransition = { slideIntoContainer(Start, tween(DEFAULT_SLIDE_ANIMATION_TWEEN)) },
    exitTransition = { slideOutOfContainer(End, tween(DEFAULT_SLIDE_ANIMATION_TWEEN)) },
  ) {
    composable<Route.MobileHome>(
      enterTransition = { slideIntoContainer(End, tween(DEFAULT_SLIDE_ANIMATION_TWEEN)) },
      exitTransition = { slideOutOfContainer(Start, tween(DEFAULT_SLIDE_ANIMATION_TWEEN)) },
    ) {
      StoryNavigationBar(
        stories = stories,
        onSelectStory = { storyId ->
          navController.navigate(Route.MobileGallery(storyId))
        },
        activeStoryId = -1,
      )
    }
    composable<Route.MobileGallery> { backStackEntry ->
      val storyId = backStackEntry.toRoute<Route.MobileGallery>().storyId
      Column {
        MobileGallery(
          story = stories.find { it.id == storyId },
          back = dropUnlessResumed {
            navController.popBackStack()
          },
        )
      }
    }
  }
}
