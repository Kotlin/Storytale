package org.jetbrains.compose.storytale.gallery.material3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.pow
import org.jetbrains.compose.storytale.StoryParameter
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import org.jetbrains.compose.storytale.gallery.ui.theme.LocalCustomDensity
import org.jetbrains.compose.storytale.gallery.ui.theme.UseCustomDensity
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
            val customComposable = parameterUiControllerCustomizer?.customComposable(parameter)
            if (customComposable != null) {
                customComposable(parameter)
            } else when (parameter.type) {
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

                ULong::class -> TextParameterField(
                    parameterName = parameter.name,
                    state = parameter.state.cast(),
                    toTypeOrNull = { toULongOrNull() },
                    label = "ULong",
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

internal var parameterUiControllerCustomizer: ParameterUiControllerCustomizer? = null

/**
 * Available for internal usages for now.
 * Might be deleted or changed in an incompatible manner. Use it at your own risk.
 */
fun interface ParameterUiControllerCustomizer {
    /**
     * Returns null if the customizer doesn't handle this [StoryParameter.type].
     * Otherwise, it must return a Composable lambda with a UI visualizing and controlling the parameter state.
     */
    fun customComposable(parameter: StoryParameter<*>): (@Composable (parameter: StoryParameter<*>) -> Unit)?
}

@Composable
fun ParameterLabel(
    name: String,
    modifier: Modifier = Modifier,
) = Box(
    modifier = modifier
        .clip(RoundedCornerShape(5.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant),
) {
    Text(
        text = name,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 8.sp,
        modifier = Modifier.padding(4.dp),
    )
}


@Composable
fun ParameterHeader(
    name: String,
    type: String,
    modifier: Modifier = Modifier
) {
    CenterRow(modifier) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
        )
        Gap(6.dp)
        ParameterLabel(type)
    }
}

@Composable
fun ParameterDescription(
    description: String
) {
    if (description.isNotEmpty()) {
        Gap(8.dp)
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
internal fun DensityParameterSlider(
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


@Suppress("ktlint:compose:mutable-state-param-check")
@Composable
fun BooleanParameterField(
    parameterName: String,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    description: String = "",
) = Column(modifier = modifier) {
    var checked by state
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        ParameterHeader(parameterName, "Boolean")

        Spacer(Modifier.weight(1f))

        UseCustomDensity {
            Switch(checked = checked, onCheckedChange = { checked = it })
        }
    }
    ParameterDescription(description)
}


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
    var value by state

    ParameterHeader(parameterName, label)
    Gap(8.dp)

    UseCustomDensity {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value.toString(),
            onValueChange = { newValue -> newValue.toTypeOrNull()?.also { value = it } },
        )
    }

    ParameterDescription(description)
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
