package storytale.gallery.demo

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import org.jetbrains.compose.storytale.story

val Demo1 by story {
    Button(onClick = {}) {
        Text("Click Me")
    }
}


fun initStories() {
    // just calling this fun to initialize the Stories declared in this file
}
