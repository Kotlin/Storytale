@file:Suppress("ktlint:standard:property-naming")

package storytale.gallery.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.story

val `Floating Action Buttons` by story {
    val Density by parameter(LocalDensity.current)
    val `Container color` by parameter(MaterialTheme.colorScheme.primary)
    val bgColor = `Container color`

    CompositionLocalProvider(LocalDensity provides Density) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            SmallFloatingActionButton(onClick = {}, containerColor = bgColor) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
            FloatingActionButton(onClick = {}, containerColor = bgColor) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
            ExtendedFloatingActionButton(onClick = {}, containerColor = bgColor) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
                Spacer(Modifier.padding(4.dp))
                Text("Extended")
            }
            LargeFloatingActionButton(onClick = {}, containerColor = bgColor) {
                Text("Large")
            }
        }
    }
}

val `Segmented buttons` by story {
    val selectedIndex = remember { mutableIntStateOf(0) }

    SingleChoiceSegmentedButtonRow {
        repeat(3) { index ->
            SegmentedButton(
                selected = index == selectedIndex.value,
                onClick = { selectedIndex.value = index },
                shape = SegmentedButtonDefaults.itemShape(index, 3),
            ) {
                Text("Button $index", modifier = Modifier.padding(4.dp))
            }
        }
    }
}

val `Common buttons` by story {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        ElevatedButton(onClick = {}) {
            Text("Elevated Button")
        }

        Button(onClick = {}) {
            Text("Filled", softWrap = false)
        }

        FilledTonalButton(onClick = {}) {
            Text("Tonal", softWrap = false)
        }

        OutlinedButton(onClick = {}) {
            Text("Outlined", softWrap = false)
        }

        TextButton(onClick = {}) {
            Text("Text", softWrap = false)
        }
    }
}
