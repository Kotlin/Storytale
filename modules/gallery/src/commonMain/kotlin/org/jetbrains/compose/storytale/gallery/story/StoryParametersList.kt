package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.reflect.KClass
import org.jetbrains.compose.storytale.StoryParameter
import org.jetbrains.compose.storytale.gallery.story.parameters.BooleanParameterField
import org.jetbrains.compose.storytale.gallery.story.parameters.ListParameter
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
            key(parameter) {
                val label = parameter.label ?: parameter.type.toLabel()
                val values = parameter.values
                if (values != null) {
                    ListParameter(
                        parameterName = parameter.name,
                        selectedValueIndex = parameter.state.cast(),
                        values = values,
                        label = label,
                    )
                } else {
                    when (parameter.type) {
                        String::class -> TextParameterField(
                            parameterName = parameter.name,
                            state = parameter.state.cast(),
                            toTypeOrNull = { toString() },
                            label = label,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Boolean::class -> BooleanParameterField(
                            parameterName = parameter.name,
                            state = parameter.state.cast(),
                            modifier = Modifier.fillMaxWidth(),
                            label = label,
                        )

                        Byte::class -> TextParameterField(
                            parameterName = parameter.name,
                            state = parameter.state.cast(),
                            toTypeOrNull = { toByteOrNull() },
                            label = label,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Short::class -> TextParameterField(
                            parameterName = parameter.name,
                            state = parameter.state.cast(),
                            toTypeOrNull = { toShortOrNull() },
                            label = label,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Int::class -> TextParameterField(
                            parameterName = parameter.name,
                            state = parameter.state.cast(),
                            toTypeOrNull = { toIntOrNull() },
                            label = label,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Long::class -> TextParameterField(
                            parameterName = parameter.name,
                            state = parameter.state.cast(),
                            toTypeOrNull = { toLongOrNull() },
                            label = label,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Float::class -> TextParameterField(
                            parameterName = parameter.name,
                            state = parameter.state.cast(),
                            toTypeOrNull = { toFloatOrNull() },
                            label = label,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Double::class -> TextParameterField(
                            parameterName = parameter.name,
                            state = parameter.state.cast(),
                            toTypeOrNull = { toDoubleOrNull() },
                            label = label,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        else -> error("Unsupported parameter type ${parameter.type}")
                    }
                }
            }
        }
    }
}

internal fun KClass<*>.toLabel(): String? = when (this) {
    String::class -> "String"
    Boolean::class -> "Boolean"
    Byte::class -> "Byte"
    Short::class -> "Short"
    Int::class -> "Int"
    Long::class -> "Long"
    Float::class -> "Float"
    Double::class -> "Double"
    else -> simpleName
}
