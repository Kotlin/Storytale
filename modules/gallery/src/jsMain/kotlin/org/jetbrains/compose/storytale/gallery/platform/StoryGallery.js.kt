package org.jetbrains.compose.storytale.gallery.platform

import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavHostController
import androidx.navigation.bindToNavigation
import kotlinx.browser.window

@OptIn(ExperimentalBrowserHistoryApi::class)
actual suspend fun bindNavigation(navController: NavHostController) {
  window.bindToNavigation(navController)
}
