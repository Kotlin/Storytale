package org.jetbrains.compose.storytale.gallery.platform

import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavHostController
import androidx.navigation.bindToBrowserNavigation

@OptIn(ExperimentalBrowserHistoryApi::class)
actual suspend fun bindNavigation(navController: NavHostController) {
    navController.bindToBrowserNavigation()
}
