@file:Suppress("ktlint:standard:property-naming")

package storytale.gallery.demo

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
    var checked by parameter(false)
    Checkbox(checked, onCheckedChange = { checked = it })
}

val Switch by story {
    var checked by parameter(false)
    Switch(checked, onCheckedChange = { checked = it })
}
