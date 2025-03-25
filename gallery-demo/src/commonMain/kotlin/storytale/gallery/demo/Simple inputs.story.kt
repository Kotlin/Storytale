package storytale.gallery.demo

import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.storytale.story

val Button by story {
    Button(onClick = {}) {
        Text("Click Me")
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
