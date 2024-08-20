package org.jetbrains.compose.storytale.gallery.platform

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.navigation.Route
import org.jetbrains.compose.storytale.gallery.platform.mobile.MobileGallery
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
    startDestination = Route.MobileHome.route,
    modifier = modifier,
    enterTransition = { slideIntoContainer(Start, tween(defaultSlideAnimationTween)) },
    exitTransition = { slideOutOfContainer(End, tween(defaultSlideAnimationTween)) }
  ) {
    composable(
      route = Route.MobileHome.route,
      enterTransition = { slideIntoContainer(End, tween(defaultSlideAnimationTween)) },
      exitTransition = { slideOutOfContainer(Start, tween(defaultSlideAnimationTween)) }
    ) {
      StoryNavigationBar(
        stories = stories,
        onSelectStory = { storyId ->
          navController.navigate(
            route = Route.MobileGallery.route.replace("{storyId}", storyId.toString())
          )
        },
        activeStoryId = -1
      )
    }
    composable(
      route = Route.MobileGallery.route,
      arguments = listOf(
        navArgument("storyId") { type = NavType.IntType }
      )
    ) { backStackEntry ->
      val storyId = backStackEntry.arguments?.getInt("storyId")
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
