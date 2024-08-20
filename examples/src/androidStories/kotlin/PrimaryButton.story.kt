import org.jetbrains.compose.storytale.story

val `PrimaryButtonDefaultState` by story {
   PrimaryButton(onClick = {})
}

val `PrimaryButtonDisabled` by story {
   PrimaryButton(onClick = {}, enabled = false)
}