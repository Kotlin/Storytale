package org.jetbrains.storytale

import StoryGallery
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ui.theme.StoryGalleryTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      StoryGalleryTheme {
        StoryGallery()
      }
    }
  }
}
