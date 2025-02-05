package org.jetbrains.compose.storytale.gallery

import androidx.compose.runtime.Composable
import org.jetbrains.compose.storytale.gallery.platform.StoryGallery
import org.jetbrains.compose.storytale.gallery.ui.theme.StoryGalleryTheme
import org.jetbrains.compose.storytale.storiesStorage

@Composable
fun Gallery() {
  StoryGalleryTheme {
    StoryGallery(storiesStorage)
  }
}
