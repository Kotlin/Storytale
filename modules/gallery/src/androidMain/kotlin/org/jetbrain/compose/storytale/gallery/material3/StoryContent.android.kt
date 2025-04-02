package org.jetbrains.compose.storytale.gallery.material3

import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import android.content.ClipData

internal actual suspend fun Clipboard.copyCodeToClipboard(code: String) {
    val clipData = ClipData.newPlainText("Copied Code", code)
    setClipEntry(ClipEntry(clipData))
}
