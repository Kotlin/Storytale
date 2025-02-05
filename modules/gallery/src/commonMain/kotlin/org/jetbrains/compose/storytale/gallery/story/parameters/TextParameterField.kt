package org.jetbrains.compose.storytale.gallery.story.parameters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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

@Suppress("ktlint:compose:mutable-state-param-check")
@Composable
fun <T> TextParameterField(
  parameterName: String,
  state: MutableState<T>,
  toTypeOrNull: String.() -> T?,
  modifier: Modifier = Modifier,
  label: String = "",
  description: String = "",
) = Column {
  var number by state
  CenterRow {
    Text(
      text = parameterName,
      style = currentTypography.parameterText,
    )
    Gap(6.dp)
    ParameterLabel(label)
  }
  Gap(12.dp)
  BasicTextField(
    value = number.toString(),
    onValueChange = { newValue -> newValue.toTypeOrNull()?.also { number = it } },
    maxLines = 1,
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(currentColorScheme.primaryText),
    textStyle = currentTextStyle.copy(
      fontSize = 14.sp,
      fontWeight = FontWeight.SemiBold,
      color = Color.White,
    ),
    cursorBrush = SolidColor(Color.White),
  ) {
    Box(Modifier.padding(12.dp)) {
      it()
    }
  }
  if (description.isNotEmpty()) {
    Gap(12.dp)
    Text(
      text = description,
      style = currentTypography.parameterDescription,
    )
  }
}
