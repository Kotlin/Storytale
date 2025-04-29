package storytale.gallery.demo

import androidx.compose.material3.TriStateCheckbox
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.state.ToggleableState.Indeterminate
import androidx.compose.ui.state.ToggleableState.Off
import androidx.compose.ui.state.ToggleableState.On
import org.jetbrains.compose.storytale.story

val `Check box` by story {
    var state by parameter(ToggleableState.entries)

    TriStateCheckbox(
        state = state,
        onClick = {
            state = when (state) {
                On -> Indeterminate
                Off -> On
                Indeterminate -> Off
            }
        },
    )
}
