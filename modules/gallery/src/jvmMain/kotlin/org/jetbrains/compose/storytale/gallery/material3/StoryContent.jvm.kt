package org.jetbrains.compose.storytale.gallery.material3

import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import java.awt.datatransfer.StringSelection

internal actual suspend fun Clipboard.copyCodeToClipboard(code: String) {
    setClipEntry(ClipEntry(StringSelection(code)))
}
