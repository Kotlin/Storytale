import org.jetbrains.compose.storytale.story

val `PrimaryButton default state` by story {
   PrimaryButton(onClick = {})
}

val `PrimaryButton disabled` by story {
   PrimaryButton(onClick = {}, enabled = false)
}