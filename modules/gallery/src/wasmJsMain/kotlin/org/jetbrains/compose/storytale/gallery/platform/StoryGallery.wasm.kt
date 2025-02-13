package org.jetbrains.compose.storytale.gallery.platform

import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavHostController
import kotlinx.browser.window
import androidx.navigation.bindToNavigation

actual suspend fun bindNavigation(navController: NavHostController) {
  @OptIn(ExperimentalBrowserHistoryApi::class)
  window.bindToNavigation(navController)
}
