import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.story

val `ComposeLogo Default State` by story {
  ComposeLogo()
}

val `ComposeLogo Small` by story {
  ComposeLogo(Modifier.size(300.dp))
}

val `ComposeLogo Medium` by story {
  ComposeLogo(Modifier.size(600.dp))
}

val `ComposeLogo Large` by story {
  ComposeLogo(Modifier.size(900.dp))
}
