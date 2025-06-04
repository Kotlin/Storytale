package org.jetbrains.compose.storytale.generated

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import org.jetbrains.compose.storytale.gallery.material3.StorytaleGalleryApp

// To let the Storytale compiler plugin add the initializations for stories
@Suppress("ktlint:standard:function-naming")
fun MainViewController() {
    singleWindowApplication(
        state = WindowState(width = 800.dp, height = 800.dp),
        alwaysOnTop = true,
    ) {
        StorytaleGalleryApp()
    }
}
