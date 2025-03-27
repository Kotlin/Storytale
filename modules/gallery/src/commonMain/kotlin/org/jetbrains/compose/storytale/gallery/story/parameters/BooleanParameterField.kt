package org.jetbrains.compose.storytale.gallery.story.parameters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import org.jetbrains.compose.storytale.gallery.ui.component.SwitchButton
import org.jetbrains.compose.storytale.gallery.ui.theme.currentTypography

@Suppress("ktlint:compose:mutable-state-param-check")
@Composable
fun BooleanParameterField(
    parameterName: String,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    description: String = "",
) = Column(modifier = modifier) {
    var checked by state
    CenterRow {
        CenterRow(Modifier.weight(1f)) {
            Text(
                text = parameterName,
                style = currentTypography.parameterText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Gap(6.dp)
            ParameterLabel("Boolean")
        }
        SwitchButton(
            checked = checked,
            onValueChange = { checked = it },
        )
    }
    if (description.isNotEmpty()) {
        Gap(12.dp)
        Text(
            text = description,
            style = currentTypography.parameterDescription,
        )
    }
}

@Suppress("ktlint:compose:mutable-state-param-check")
@Composable
fun BooleanParameterField2(
    parameterName: String,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    description: String = "",
) = Column(modifier = modifier) {
    var checked by state
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        ParameterHeader(parameterName, "Boolean")

        Spacer(Modifier.weight(1f))

        CompositionLocalProvider(LocalDensity provides Density(density = 1.7f)) {
            Switch(checked = checked, onCheckedChange = { checked = it })
        }
    }
    ParameterDescription(description)
}
