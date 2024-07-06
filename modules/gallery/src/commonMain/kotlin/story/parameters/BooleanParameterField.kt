package org.jetbrains.compose.storytale.gallery.story.parameters

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.SwitchButton
import org.jetbrains.compose.storytale.gallery.ui.theme.currentTypography

@Composable
fun BooleanParameterField(
  parameterName: String,
  defaultVale: Boolean,
  modifier: Modifier = Modifier,
) = CenterRow(modifier = modifier) {
  var enabled by remember(defaultVale) {
    mutableStateOf(defaultVale)
  }
  Text(
    text = parameterName,
    style = currentTypography.parameterText,
    modifier = Modifier.weight(1f)
  )
  SwitchButton(
    checked = enabled,
    onValueChange = { enabled = it }
  )
}
