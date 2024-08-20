import org.jetbrains.compose.storytale.story

val `Primary Button Default State` by story {
   PrimaryButton(onClick = {})
}

val `Primary Button Disabled` by story {
   PrimaryButton(onClick = {}, enabled = false)
} 