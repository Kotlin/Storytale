@file:Suppress("ktlint:standard:property-naming")

package storytale.gallery.demo

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.storytale.story

val Button by story {
    val Label by parameter("Click Me")
    val Enabled by parameter(true)
    val bgColorAlpha by parameter(1f)

    Button(
        enabled = Enabled,
        onClick = {},
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = bgColorAlpha),
        ),
    ) {
        Text(Label)
    }
}

val Checkbox by story {
    val checked = remember { mutableStateOf(false) }
    Checkbox(checked.value, onCheckedChange = { checked.value = it })
}

val Switch by story {
    val checked = remember { mutableStateOf(false) }
    Switch(checked.value, onCheckedChange = { checked.value = it })
}
