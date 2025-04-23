package org.jetbrains.compose.storytale.gallery.material3

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard

internal actual suspend fun Clipboard.copyCodeToClipboard(code: String) {
    val clipData = ClipData.newPlainText("Copied Code", code)
    setClipEntry(ClipEntry(clipData))
}
