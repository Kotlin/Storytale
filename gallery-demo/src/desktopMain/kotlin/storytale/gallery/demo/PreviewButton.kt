package storytale.gallery.demo

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.previewParameter

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
fun PreviewExtendedFAB() {
    val bgColor by previewParameter(MaterialTheme.colorScheme.primary)

    ExtendedFloatingActionButton(onClick = {}, containerColor = bgColor) {
        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
        Spacer(Modifier.padding(4.dp))
        Text("Extended")
    }
}
