package org.jetbrains.compose.storytale.gallery.navigation

// TODO Prepare for a type-safe approach later
// @kotlinx.serialization.Serializable
// data object Route {
//  @Serializable
//  data object MobileHome
//
//  @kotlinx.serialization.Serializable
//  data class MobileGallery(val storyId: Int)
//
//  @kotlinx.serialization.Serializable
//  data class DesktopGallery(val storyId: Int?)
// }

enum class Route(val route: String) {
  MobileHome("mobile_home"),
  MobileGallery("mobile/gallery/{storyId}")
}
