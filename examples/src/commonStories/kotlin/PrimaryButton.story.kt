import org.jetbrains.compose.storytale.story


val PrimaryButton by story {
  val enabled by parameter(true)
  val text by parameter("Click Me \uD83C\uDF1F")

  PrimaryButton(text = text, enabled = enabled)
}

val `PrimaryButton Default State` by story {
  PrimaryButton("Click Me \uD83C\uDF1F")
}

val `PrimaryButton Disabled` by story {
  PrimaryButton(
    text = "Click Me \uD83C\uDF1F",
    enabled = false
  )
}
