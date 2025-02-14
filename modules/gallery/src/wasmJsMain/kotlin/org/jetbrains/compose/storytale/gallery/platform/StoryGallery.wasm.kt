package org.jetbrains.compose.storytale.gallery.platform

import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavHostController
import androidx.navigation.bindToNavigation
import kotlinx.browser.window

actual suspend fun bindNavigation(navController: NavHostController) {
  @OptIn(ExperimentalBrowserHistoryApi::class)
  window.bindToNavigation(navController)
}
