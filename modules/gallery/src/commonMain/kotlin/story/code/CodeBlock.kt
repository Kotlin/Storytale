package org.jetbrains.compose.storytale.gallery.story.code

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CodeBlock(
  code: String,
  modifier: Modifier = Modifier
) = Row(
  modifier = modifier.background(Color.White)
) {
  var codeLines by remember { mutableIntStateOf(0) }
  val codeVerticalScrollState = rememberScrollState()
  Column(
    modifier = Modifier.background(Color(0xFFEEF0F5))
      .verticalScroll(codeVerticalScrollState)
      .padding(bottom = 6.dp)
  ) {
    repeat(codeLines) {
      Text(
        text = "${it + 1}",
        color = Color(0xFF25272A),
        fontSize = 16.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 12.dp)
      )
    }
  }
  SelectionContainer(Modifier.fillMaxSize()) {
    Text(
      text = code,
      color = Color(0xFF252B30),
      fontSize = 16.sp,
      lineHeight = 28.sp,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier
        .fillMaxSize()
        .horizontalScroll(rememberScrollState())
        .verticalScroll(codeVerticalScrollState)
        .padding(start = 12.dp, end = 12.dp, bottom = 16.dp),
      onTextLayout = {
        codeLines = it.lineCount
      }
    )
  }
}
