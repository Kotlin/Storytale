package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.pow
import org.jetbrains.compose.storytale.StoryParameter
import org.jetbrains.compose.storytale.gallery.story.parameters.BooleanParameterField
import org.jetbrains.compose.storytale.gallery.story.parameters.BooleanParameterField2
import org.jetbrains.compose.storytale.gallery.story.parameters.ParameterHeader
import org.jetbrains.compose.storytale.gallery.story.parameters.TextParameterField
import org.jetbrains.compose.storytale.gallery.story.parameters.TextParameterField2
import org.jetbrains.compose.storytale.gallery.ui.theme.LocalCustomDensity
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


@Composable
fun StoryParametersList2(
    parameters: List<StoryParameter<*>>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        parameters.forEach { parameter ->
            when (parameter.type) {
                String::class -> TextParameterField2(
                    parameterName = parameter.name,
                    state = parameter.state.cast(),
                    toTypeOrNull = { toString() },
                    label = "String",
                    modifier = Modifier.fillMaxWidth(),
                )

                Boolean::class -> BooleanParameterField2(
                    parameterName = parameter.name,
                    state = parameter.state.cast(),
                    modifier = Modifier.fillMaxWidth(),
                )

                Byte::class -> TextParameterField2(
                    parameterName = parameter.name,
                    state = parameter.state.cast(),
                    toTypeOrNull = { toByteOrNull() },
                    label = "Byte",
                    modifier = Modifier.fillMaxWidth(),
                )

                Short::class -> TextParameterField2(
                    parameterName = parameter.name,
                    state = parameter.state.cast(),
                    toTypeOrNull = { toShortOrNull() },
                    label = "Short",
                    modifier = Modifier.fillMaxWidth(),
                )

                Int::class -> TextParameterField2(
                    parameterName = parameter.name,
                    state = parameter.state.cast(),
                    toTypeOrNull = { toIntOrNull() },
                    label = "Int",
                    modifier = Modifier.fillMaxWidth(),
                )

                Long::class -> TextParameterField2(
                    parameterName = parameter.name,
                    state = parameter.state.cast(),
                    toTypeOrNull = { toLongOrNull() },
                    label = "Long",
                    modifier = Modifier.fillMaxWidth(),
                )

                ULong::class -> TextParameterField2(
                    parameterName = parameter.name,
                    state = parameter.state.cast(),
                    toTypeOrNull = { toULongOrNull() },
                    label = "ULong",
                    modifier = Modifier.fillMaxWidth(),
                )

                Float::class -> TextParameterField2(
                    parameterName = parameter.name,
                    state = parameter.state.cast(),
                    toTypeOrNull = { toFloatOrNull() },
                    label = "Float",
                    modifier = Modifier.fillMaxWidth(),
                )

                Double::class -> TextParameterField2(
                    parameterName = parameter.name,
                    state = parameter.state.cast(),
                    toTypeOrNull = { toDoubleOrNull() },
                    label = "Double",
                    modifier = Modifier.fillMaxWidth(),
                )

                Density::class -> DensityParameterSlider(
                    parameterName = parameter.name,
                    state = parameter.state.cast(),
                    modifier = Modifier.fillMaxWidth(),
                )

                else -> error("Unsupported parameter type ${parameter.type}")
            }
        }
    }
}

@Composable
fun DensityParameterSlider(
    parameterName: String,
    state: MutableState<Density>,
    modifier: Modifier = Modifier
) {

    BoxWithConstraints(modifier = modifier) {
        ParameterHeader(parameterName, "Density")

        val realDensity = LocalDensity.current.density

        Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp), verticalAlignment = CenterVertically) {
            CompositionLocalProvider(LocalDensity provides LocalCustomDensity.current) {
                Slider(
                    modifier = Modifier.weight(0.75f),
                    value = state.value.density,
                    valueRange = 0.25f..realDensity + 2f,
                    onValueChange = {
                        state.value = Density(it)
                    }
                )
            }

            Text(
                text = state.value.density.simpleFormat(1),
                modifier = Modifier.weight(0.15f).padding(4.dp),
                textAlign = TextAlign.End,
            )
        }
    }

}

private fun Number.simpleFormat(numberDigitsAfterSeparator: Int = 0, decimalSeparator: Char = '.'): String {
    if (numberDigitsAfterSeparator < 0)
        throw IllegalArgumentException("numberDigitsAfterSeparator should be >= 0 but is $numberDigitsAfterSeparator")

    val prefix = this.toInt()
    if (numberDigitsAfterSeparator == 0) return "$prefix"

    val sign = if (this.toDouble() >= 0.0) "" else "-"

    val afterSeparatorPart = abs(this.toDouble() - prefix)
    val suffixInt = (10.0.pow(numberDigitsAfterSeparator) * afterSeparatorPart).toInt()
    val suffix = if (afterSeparatorPart >= 1.0) "$suffixInt" else addNullsBefore(suffixInt, numberDigitsAfterSeparator)
    return "$sign${abs(prefix)}$decimalSeparator$suffix"
}

private fun addNullsBefore(suffixInt: Int, numberDigitsAfterSeparator: Int): String {
    var s = "$suffixInt"
    val len = s.length
    repeat(numberDigitsAfterSeparator - len) { _ -> s = "0$s" }
    return s
}
