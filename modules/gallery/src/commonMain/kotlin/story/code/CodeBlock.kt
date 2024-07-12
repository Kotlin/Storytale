package org.jetbrains.compose.storytale.gallery.story.code

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.storytale.gallery.compose.clickableWithoutRipple
import org.jetbrains.compose.storytale.gallery.generated.resources.Res
import org.jetbrains.compose.storytale.gallery.generated.resources.copy
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow

@Composable
fun CodeBlock(
  code: String,
  modifier: Modifier = Modifier
) = Box(
  modifier = modifier.clip(RoundedCornerShape(14.dp))
    .height(IntrinsicSize.Min)
    .background(Color(0xFF282A2E))
) {
  val clipboard = LocalClipboardManager.current
  Column(
    modifier = Modifier.padding(vertical = 10.dp)
  ) {
    CenterRow(Modifier.padding(horizontal = 12.dp)) {
      Text(
        text = "Story Code Snippet",
        color = Color(0xFF99B3F3),
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.weight(1f)
      )
      Icon(
        painter = painterResource(Res.drawable.copy),
        contentDescription = null,
        modifier = Modifier.size(20.dp).clickableWithoutRipple {
          clipboard.setText(AnnotatedString(code))
        },
        tint = Color.White
      )
    }
    HorizontalDivider(
      modifier = Modifier.padding(vertical = 12.dp),
      color = Color.White,
      thickness = 0.2.dp
    )
    Text(
      text = code,
      color = Color.White,
      fontSize = 14.sp,
      fontWeight = FontWeight.Normal,
      maxLines = 15,
      modifier = Modifier.padding(horizontal = 12.dp)
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .verticalScroll(rememberScrollState())
    )
  }
}
