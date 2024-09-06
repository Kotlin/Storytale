import org.jetbrains.compose.storytale.story


val PrimaryButton by story {
  val enabled by parameter(true)
  val text by parameter("Click Me")

  PrimaryButton(text = text, enabled = enabled)
}

val `PrimaryButton Default State` by story {
  PrimaryButton("Click Me")
}

val `PrimaryButton Disabled` by story {
  PrimaryButton(
    text = "Click Me",
    enabled = false
  )
}
