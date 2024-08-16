package org.jetbrains.compose.storytale.gallery.platform.desktop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.storytale.gallery.event.Event
import org.jetbrains.compose.storytale.gallery.event.send
import org.jetbrains.compose.storytale.gallery.generated.resources.Res
import org.jetbrains.compose.storytale.gallery.generated.resources.copy
import org.jetbrains.compose.storytale.gallery.generated.resources.story_widget_icon
import org.jetbrains.compose.storytale.gallery.story.code.CodeBlock
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import org.jetbrains.compose.storytale.gallery.ui.component.IconButton
import org.jetbrains.compose.storytale.gallery.ui.theme.currentColorScheme

@Composable
fun DesktopCodeBlock(
  code: String,
  storyName: String,
  modifier: Modifier = Modifier
) = Column(modifier) {
  val clipboard = LocalClipboardManager.current
  CenterRow(Modifier.padding(12.dp)) {
    CenterRow(Modifier.weight(1f)) {
      Icon(
        painter = painterResource(Res.drawable.story_widget_icon),
        contentDescription = null,
        modifier = Modifier.size(18.dp),
        tint = currentColorScheme.primaryText
      )
      Gap(4.dp)
      Text(
        text = storyName,
        color = currentColorScheme.primaryText,
        modifier = Modifier.weight(1f),
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp
      )
    }
    IconButton(
      onClick = {
        Event.CopyCode.send()
        clipboard.setText(AnnotatedString(code))
      }
    ) {
      Icon(
        painter = painterResource(Res.drawable.copy),
        contentDescription = "Copy code",
        tint = Color(0xFF5448D9)
      )
    }
  }
  HorizontalDivider(color = Color(0xFFE1E1E1), thickness = 0.67.dp)
  CodeBlock(
    code = code,
    modifier = Modifier.fillMaxSize()
  )
}
