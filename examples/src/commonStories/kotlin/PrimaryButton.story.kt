import org.jetbrains.compose.storytale.story

val `PrimaryButton default state` by story {
   val enabled by parameter(true)
   val text by parameter("Button Name")

   PrimaryButton(
      text = text,
      onClick = {},
      enabled = enabled
   )
}