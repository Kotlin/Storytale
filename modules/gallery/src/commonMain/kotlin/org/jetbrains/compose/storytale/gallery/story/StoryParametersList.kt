package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.StoryParameter
import org.jetbrains.compose.storytale.gallery.story.parameters.BooleanParameterField
import org.jetbrains.compose.storytale.gallery.story.parameters.TextParameterField
import org.jetbrains.compose.storytale.gallery.utils.cast

@Composable
fun StoryParametersList(
  parameters: List<StoryParameter<*>>,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(24.dp),
  ) {
    parameters.forEach { parameter ->
      when (parameter.type) {
        String::class -> TextParameterField(
          parameterName = parameter.name,
          state = parameter.state.cast(),
          toTypeOrNull = { toString() },
          label = "String",
          modifier = Modifier.fillMaxWidth(),
        )

        Boolean::class -> BooleanParameterField(
          parameterName = parameter.name,
          state = parameter.state.cast(),
          modifier = Modifier.fillMaxWidth(),
        )

        Byte::class -> TextParameterField(
          parameterName = parameter.name,
          state = parameter.state.cast(),
          toTypeOrNull = { toByteOrNull() },
          label = "Byte",
          modifier = Modifier.fillMaxWidth(),
        )

        Short::class -> TextParameterField(
          parameterName = parameter.name,
          state = parameter.state.cast(),
          toTypeOrNull = { toShortOrNull() },
          label = "Short",
          modifier = Modifier.fillMaxWidth(),
        )

        Int::class -> TextParameterField(
          parameterName = parameter.name,
          state = parameter.state.cast(),
          toTypeOrNull = { toIntOrNull() },
          label = "Int",
          modifier = Modifier.fillMaxWidth(),
        )

        Long::class -> TextParameterField(
          parameterName = parameter.name,
          state = parameter.state.cast(),
          toTypeOrNull = { toLongOrNull() },
          label = "Long",
          modifier = Modifier.fillMaxWidth(),
        )

        Float::class -> TextParameterField(
          parameterName = parameter.name,
          state = parameter.state.cast(),
          toTypeOrNull = { toFloatOrNull() },
          label = "Float",
          modifier = Modifier.fillMaxWidth(),
        )

        Double::class -> TextParameterField(
          parameterName = parameter.name,
          state = parameter.state.cast(),
          toTypeOrNull = { toDoubleOrNull() },
          label = "Double",
          modifier = Modifier.fillMaxWidth(),
        )

        else -> error("Unsupported parameter type ${parameter.type}")
      }
    }
  }
}
