import org.jetbrains.compose.storytale.story

val PrimaryButton by story {
    val size by parameter(PrimaryButtonSize.Medium)
    val enabled by parameter(true)
    val text by parameter("Click Me")

    PrimaryButton(text = text, enabled = enabled, size = size)
}

val `PrimaryButton Default State` by story {
    PrimaryButton("Click Me")
}

val `PrimaryButton Disabled` by story {
    PrimaryButton(
        text = "Click Me",
        enabled = false,
    )
}

val `PrimaryButton Food` by story {
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

    val food by parameter(foodEmojiList)
    PrimaryButton(text = food)
}
