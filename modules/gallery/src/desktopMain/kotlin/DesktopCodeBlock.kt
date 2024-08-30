import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import dev.snipme.highlights.model.SyntaxTheme
import dev.snipme.highlights.model.SyntaxThemes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.storytale.gallery.event.Event
import org.jetbrains.compose.storytale.gallery.event.send
import org.jetbrains.compose.storytale.gallery.generated.resources.Res
import org.jetbrains.compose.storytale.gallery.generated.resources.check
import org.jetbrains.compose.storytale.gallery.generated.resources.copy
import org.jetbrains.compose.storytale.gallery.generated.resources.palette
import org.jetbrains.compose.storytale.gallery.generated.resources.story_widget_icon
import org.jetbrains.compose.storytale.gallery.story.code.CodeBlock
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import org.jetbrains.compose.storytale.gallery.ui.component.IconButton
import org.jetbrains.compose.storytale.gallery.ui.theme.currentColorScheme

@Composable
fun DesktopCodeBlock(
  code: String,
  storyName: String,
  modifier: Modifier = Modifier
) = Column(modifier) {
  val clipboard = LocalClipboardManager.current
  var showCodeHighlightTheme by remember { mutableStateOf(false) }
  var iconOffset by remember { mutableStateOf(IntOffset.Zero) }

  var codeHighlightTheme by remember {
    mutableStateOf(SyntaxThemes.pastel())
  }

  CenterRow(Modifier.padding(12.dp)) {
    CenterRow(Modifier.weight(1f)) {
      Icon(
        painter = painterResource(Res.drawable.story_widget_icon),
        contentDescription = null,
        modifier = Modifier.size(18.dp),
        tint = currentColorScheme.primaryText
      )
      Gap(4.dp)
      Text(
        text = storyName,
        color = currentColorScheme.primaryText,
        modifier = Modifier.weight(1f),
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp
      )
    }
    CenterRow(
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Column {
        IconButton(
          onClick = {
            showCodeHighlightTheme = true
          },
          modifier = Modifier.onSizeChanged { iconOffset = IntOffset(it.width, it.height) }
        ) {
          Icon(
            painter = painterResource(Res.drawable.palette),
            contentDescription = "code highlight theme",
            tint = currentColorScheme.primaryText,
            modifier = Modifier.size(24.dp)
          )
        }
        CodeHighlightThemePanel(
          visible = showCodeHighlightTheme,
          currentTheme = codeHighlightTheme,
          initialOffset = IntOffset(-iconOffset.x * 2, 0),
          onDismissRequest = { showCodeHighlightTheme = false },
          onChangeTheme = { codeHighlightTheme = it }
        )
      }
      IconButton(
        onClick = {
          Event.CopyCode.send()
          clipboard.setText(AnnotatedString(code))
        }
      ) {
        Icon(
          painter = painterResource(Res.drawable.copy),
          contentDescription = "Copy code",
          tint = currentColorScheme.primaryText,
          modifier = Modifier.size(24.dp)
        )
      }
    }
  }
  HorizontalDivider(color = Color(0xFFE1E1E1), thickness = 0.67.dp)
  CodeBlock(
    code = code,
    theme = codeHighlightTheme,
    modifier = Modifier.fillMaxSize()
  )
}

@Composable
private fun CodeHighlightThemePanel(
  visible: Boolean,
  currentTheme: SyntaxTheme,
  initialOffset: IntOffset,
  onChangeTheme: (SyntaxTheme) -> Unit,
  onDismissRequest: () -> Unit,
) = AnimatedVisibility(
  visible = visible,
  enter = EnterTransition.None,
  exit = ExitTransition.None
) {
  Popup(
    onDismissRequest = onDismissRequest,
    offset = initialOffset
  ) {
    Column(
      modifier = Modifier.animateEnterExit(
        enter = EnterTransition.None,
        exit = fadeOut(tween(350))
      ).background(Color.White, RoundedCornerShape(12.dp))
        .border(0.7.dp, Color.Gray, RoundedCornerShape(12.dp))
        .clip(RoundedCornerShape(12.dp))
        .width(IntrinsicSize.Min)
    ) {
      SyntaxThemes.getNames().forEach {
        CenterRow(
          modifier = Modifier.fillMaxWidth().clickable {
            onChangeTheme(SyntaxThemes.light[it.toLowerCase(Locale.current)]!!)
          }
            .padding(8.dp)
        ) {
          Text(
            text = it,
            fontWeight = FontWeight.Medium,
            color = currentColorScheme.primaryText
          )
          if (currentTheme.key.replaceFirstChar { c -> c.uppercase() } == it) {
            Gap(6.dp)
            Icon(
              painter = painterResource(Res.drawable.check),
              contentDescription = null,
              tint = Color(0xFF5FAD65),
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }
  }
}
