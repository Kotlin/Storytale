package org.jetbrains.compose.storytale.generated

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import org.jetbrains.compose.storytale.gallery.ui.theme.ColorScheme
import org.jetbrains.compose.storytale.gallery.ui.theme.LocalColorScheme
import org.jetbrains.storytale.gallery2.Testing

// To let the Storytale compiler plugin add the initializations for stories
fun MainViewController() {
    singleWindowApplication(
        state = WindowState(width = 1400.dp, height = 800.dp),
    ) {
        DevelopmentEntryPoint {
            CompositionLocalProvider(LocalColorScheme provides ColorScheme.Light) {
                Testing()
            }
        }
    }
}
