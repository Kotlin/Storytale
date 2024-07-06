package org.jetbrains.compose.storytale.gallery.story.parameters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.storytale.gallery.compose.currentTextStyle
import org.jetbrains.compose.storytale.gallery.generated.resources.Res
import org.jetbrains.compose.storytale.gallery.generated.resources.edit
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import org.jetbrains.compose.storytale.gallery.ui.theme.currentColorScheme

@Composable
fun StringParameterField(
  parameterName: String,
  defaultString: String,
  modifier: Modifier = Modifier
) = Column {
  var text by remember { mutableStateOf(defaultString) }
  Text(
    text = parameterName,
    fontSize = 17.sp,
    fontWeight = FontWeight.Medium,
    color = currentColorScheme.primaryText.copy(.67f),
  )
  Gap(12.dp)
  BasicTextField(
    value = text,
    onValueChange = {
      text = it
    },
    maxLines = 1,
    modifier = modifier.clip(RoundedCornerShape(12.dp))
      .background(currentColorScheme.primaryText),
    textStyle = currentTextStyle.copy(
      fontSize = 14.sp,
      fontWeight = FontWeight.SemiBold,
      color = Color.White
    ),
    cursorBrush = SolidColor(Color.White)
  ) {
    CenterRow(Modifier.padding(12.dp)) {
      Icon(
        painter = painterResource(Res.drawable.edit),
        contentDescription = null,
        modifier = Modifier.size(18.dp),
        tint = Color.White
      )
      Gap(10.dp)
      it()
    }
  }
}
