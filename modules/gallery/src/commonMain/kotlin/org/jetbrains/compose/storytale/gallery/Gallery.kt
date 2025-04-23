package org.jetbrains.compose.storytale.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.storytale.gallery.material3.StorytaleGalleryApp
import org.jetbrains.compose.storytale.gallery.platform.bindNavigation

@Suppress("unused") // The call of this function is added implicitly in the generated code
@Composable
fun Gallery() {
    StorytaleGalleryApp()
}

@Composable
fun Gallery(isEmbedded: Boolean) {
    val navHostController = rememberNavController()

    StorytaleGalleryApp(isEmbedded = isEmbedded, navHostController = navHostController)

    LaunchedEffect(Unit) {
        bindNavigation(navHostController)
    }
}
