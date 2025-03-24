package org.jetbrains.compose.storytale.gallery.material3

import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard

internal actual suspend fun Clipboard.copyCodeToClipboard(code: String) {
    this.setClipEntry(ClipEntry.withPlainText(code))
}
