package org.jetbrains.compose.storytale.gallery.compose

import androidx.compose.ui.text.AnnotatedString.Builder
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle

fun Builder.text(text: String, style: SpanStyle = SpanStyle()) = withStyle(style = style) {
  append(text)
}
