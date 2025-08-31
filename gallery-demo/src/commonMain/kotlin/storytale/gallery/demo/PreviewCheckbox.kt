package storytale.gallery.demo

import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.state.ToggleableState.Indeterminate
import androidx.compose.ui.state.ToggleableState.Off
import androidx.compose.ui.state.ToggleableState.On
import org.jetbrains.compose.storytale.previewParameter
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
@Suppress("ktlint")
internal fun PreviewCheckbox() {
    var state by previewParameter(ToggleableState.entries)

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
