package storytale.gallery.demo

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.story

val foodEmojiList = listOf(
    "Apple 🍎",
    "Banana 🍌",
    "Cherry 🍒",
    "Grapes 🍇",
    "Strawberry 🍓",
    "Watermelon 🍉",
    "Pineapple 🍍",
    "Pizza 🍕",
    "Burger 🍔",
    "Fries 🍟",
    "Ice Cream 🍦",
    "Cake 🍰",
    "Coffee ☕",
    "Beer 🍺",
)

val `List Parameters` by story {
    val food by parameter(foodEmojiList)

    Button(onClick = {}) {
        Text(food)
    }
}

enum class PrimaryButtonSize {
    Small,
    Medium,
    Large,
}

val `Enum Parameters` by story {
    val size by parameter(PrimaryButtonSize.Medium, label = null)

    Button(
        onClick = {},
        modifier = Modifier.size(
            when (size) {
                PrimaryButtonSize.Small -> 90.dp
                PrimaryButtonSize.Medium -> 120.dp
                PrimaryButtonSize.Large -> 150.dp
            },
        ),
    ) {
        Text(size.toString())
    }
}
