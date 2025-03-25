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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.story

val `Floating Action Buttons` by story {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        SmallFloatingActionButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
        FloatingActionButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
        ExtendedFloatingActionButton(onClick = {}) {
            Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
            Spacer(Modifier.padding(4.dp))
            Text("Extended")
        }
        LargeFloatingActionButton(onClick = {}) {
            Text("Large")
        }
    }
}

val `Segmented buttons` by story {
    val selectedIndex = remember { mutableStateOf(0) }

    SingleChoiceSegmentedButtonRow {
        repeat(3) { index ->
            SegmentedButton(
                selected = index == selectedIndex.value,
                onClick = { selectedIndex.value = index },
                shape = SegmentedButtonDefaults.itemShape(index, 3)
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
            Text("Filled")
        }

        FilledTonalButton(onClick = {}) {
            Text("Tonal")
        }

        OutlinedButton(onClick = {}) {
            Text("Outlined")
        }

        TextButton(onClick = {}) {
            Text("Text")
        }
    }
}

