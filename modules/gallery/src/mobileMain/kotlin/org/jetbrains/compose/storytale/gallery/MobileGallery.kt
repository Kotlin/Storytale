package org.jetbrains.compose.storytale.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.generated.resources.Res
import org.jetbrains.compose.storytale.gallery.generated.resources.arrow_back
import org.jetbrains.compose.storytale.gallery.generated.resources.wrench
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import org.jetbrains.compose.storytale.gallery.ui.component.IconButton
import org.jetbrains.compose.storytale.gallery.ui.component.sheet.rememberStoryBottomSheetState
import org.jetbrains.compose.storytale.gallery.ui.theme.currentColorScheme

@Composable
fun MobileGallery(
  story: Story?,
  modifier: Modifier = Modifier,
  back: () -> Unit
) = Box(
  modifier = modifier.fillMaxSize()
    .background(Color.White)
) {
  val sheetState = rememberStoryBottomSheetState(skipPartiallyExpanded = true)
  Column {
    CenterRow(Modifier.padding(horizontal = 14.dp, vertical = 16.dp)) {
      IconButton(onClick = back) {
        Icon(
          painter = painterResource(Res.drawable.arrow_back),
          contentDescription = "back",
          modifier = Modifier.size(20.dp),
          tint = currentColorScheme.primaryText
        )
      }
      Gap(width = 14.dp)
      Text(
        text = story?.name.orEmpty(),
        fontSize = 20.sp,
        color = currentColorScheme.primaryText,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.weight(1f),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      IconButton(
        onClick = sheetState::show,
        modifier = Modifier.padding(start = 6.dp)
      ) {
        Icon(
          painter = painterResource(Res.drawable.wrench),
          contentDescription = null,
          modifier = Modifier.size(24.dp),
          tint = currentColorScheme.primaryText
        )
      }
    }
    HorizontalDivider(thickness = 0.5.dp, color = currentColorScheme.divider)
    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
      with(story!!) {
        content()
      }
    }
    MobileStoryParameterSheet(sheetState, story!!)
  }
}
