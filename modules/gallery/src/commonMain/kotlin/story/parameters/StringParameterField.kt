package org.jetbrains.compose.storytale.gallery.story.parameters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.gallery.compose.currentTextStyle
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import org.jetbrains.compose.storytale.gallery.ui.theme.currentColorScheme
import org.jetbrains.compose.storytale.gallery.ui.theme.currentTypography

@Composable
fun StringParameterField(
  parameterName: String,
  state: MutableState<String>,
  description: String = "",
  modifier: Modifier = Modifier
) = Column {
  var text by state
  CenterRow {
    Text(
      text = parameterName,
      style = currentTypography.parameterText,
      modifier = Modifier.weight(1f)
    )
    ParameterLabel("String")
  }
  Gap(12.dp)
  BasicTextField(
    value = text,
    onValueChange = { text = it },
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
    Box(Modifier.padding(12.dp)) {
      it()
    }
  }
  if (description.isNotEmpty()) {
    Gap(12.dp)
    Text(
      text = description,
      style = currentTypography.parameterDescription
    )
  }
}
