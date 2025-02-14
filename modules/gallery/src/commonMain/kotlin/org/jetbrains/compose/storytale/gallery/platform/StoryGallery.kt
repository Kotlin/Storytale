package org.jetbrains.compose.storytale.gallery.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.BigScreenGallery
import org.jetbrains.compose.storytale.gallery.SmallScreenGallery
import org.jetbrains.compose.storytale.gallery.utils.ScreenSize
import org.jetbrains.compose.storytale.gallery.utils.isMobile

expect suspend fun bindNavigation(navController: NavHostController)

@Composable
fun StoryGallery(
    stories: List<Story>,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        bindNavigation(navController)
    }

    if (ScreenSize.isMobile) {
        SmallScreenGallery(stories, navController, modifier)
    } else {
        BigScreenGallery(stories, modifier)
    }
}
