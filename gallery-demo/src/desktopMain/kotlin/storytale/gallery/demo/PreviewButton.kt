package storytale.gallery.demo

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.previewParameter

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
@Suppress("ktlint")
private fun PreviewExtendedFAB() {
    val bgColor by previewParameter(MaterialTheme.colorScheme.primary)

    ExtendedFloatingActionButton(onClick = {}, containerColor = bgColor) {
        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
        Spacer(Modifier.padding(4.dp))
        Text("Extended")
    }
}

@androidx.compose.desktop.ui.tooling.preview.Preview
@Composable
@Suppress("ktlint")
private fun PreviewSegmentedButton() {
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
