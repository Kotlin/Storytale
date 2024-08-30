package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.storytale.gallery.compose.text
import org.jetbrains.compose.storytale.gallery.generated.resources.Res
import org.jetbrains.compose.storytale.gallery.generated.resources.empty_status
import org.jetbrains.compose.storytale.gallery.ui.component.Gap

@Composable
fun StoryEmptyStatus(
  modifier: Modifier = Modifier
) = Column(
  modifier = modifier.fillMaxWidth()
    .padding(top = 72.dp)
    .padding(horizontal = 36.dp),
  horizontalAlignment = Alignment.CenterHorizontally
) {
  Icon(
    painter = painterResource(Res.drawable.empty_status),
    contentDescription = null
  )
  Gap(38.dp)
  Text(
    text = buildAnnotatedString {
      text(
        text = "It looks like you haven't created a Story yet",
        style = SpanStyle(
          color = Color(0xFF2B2F33).copy(.5f),
          fontSize = 15.sp,
          fontWeight = FontWeight.Medium
        )
      )
      text(
        text = " 🤔",
        style = SpanStyle(
          color = Color(0xFF2B2F33),
          fontSize = 15.sp,
          fontWeight = FontWeight.Medium
        )
      )
    }
  )
}
