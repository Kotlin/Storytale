package org.jetbrains.compose.storytale.gallery.story.parameters

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import org.jetbrains.compose.storytale.gallery.ui.component.SwitchButton
import org.jetbrains.compose.storytale.gallery.ui.theme.currentTypography

@Composable
fun BooleanParameterField(
  parameterName: String,
  defaultVale: Boolean,
  description: String = "",
  modifier: Modifier = Modifier,
) = Column(modifier = modifier) {
  CenterRow {
    var enabled by remember(defaultVale) {
      mutableStateOf(defaultVale)
    }
    CenterRow(Modifier.weight(1f)) {
      Text(
        text = parameterName,
        style = currentTypography.parameterText,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Gap(6.dp)
      ParameterLabel("Boolean")
    }
    SwitchButton(
      checked = enabled,
      onValueChange = { enabled = it }
    )
  }
  if (description.isNotEmpty()) {
    Gap(12.dp)
    Text(
      text = description,
      style = currentTypography.parameterDescription
    )
  }
}
