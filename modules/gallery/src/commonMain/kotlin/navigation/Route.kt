package org.jetbrains.compose.storytale.gallery.navigation

import kotlinx.serialization.Serializable

 @Serializable
 data object Route {
  @Serializable
  data object MobileHome

  @Serializable
  data class MobileGallery(val storyId: Int)

  @Serializable
  data class DesktopGallery(val storyId: Int?)
}
