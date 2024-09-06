import org.jetbrains.compose.storytale.story


val `ComposeLogo Default State` by story {
  ComposeLogo()
}

val `ComposeLogo Small` by story {
  ComposeLogo(size = LogoSize.SMALL)
}

val `ComposeLogo Medium` by story {
  ComposeLogo(size = LogoSize.MEDIUM)
}

val `ComposeLogo Large` by story {
  ComposeLogo(size = LogoSize.LARGE)
}
