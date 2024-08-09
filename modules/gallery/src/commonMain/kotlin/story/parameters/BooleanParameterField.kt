package org.jetbrains.compose.storytale.gallery.story.parameters

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import org.jetbrains.compose.storytale.gallery.ui.component.SwitchButton
import org.jetbrains.compose.storytale.gallery.ui.theme.currentTypography

@Composable
fun BooleanParameterField(
  parameterName: String,
  state: MutableState<Boolean>,
  description: String = "",
  modifier: Modifier = Modifier,
) = Column(modifier = modifier) {
  var checked by state

  CenterRow {
    Text(
      text = parameterName,
      style = currentTypography.parameterText,
      modifier = Modifier.weight(1f)
    )
    ParameterLabel("Boolean")
  }
  SwitchButton(
    checked = checked,
    onValueChange = { checked = it }
  )
  Gap(12.dp)
  if (description.isNotEmpty()) {
    Gap(12.dp)
    Text(
      text = description,
      style = currentTypography.parameterDescription
    )
  }
}
