package org.jetbrains.compose.storytale.gallery.story.code

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.BoldHighlight
import dev.snipme.highlights.model.ColorHighlight
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxTheme
import dev.snipme.highlights.model.SyntaxThemes
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.storytale.gallery.compose.text
import org.jetbrains.compose.storytale.gallery.generated.resources.JetBrainsMono_Regular
import org.jetbrains.compose.storytale.gallery.generated.resources.Res

@Composable
fun CodeBlock(
    code: String,
    modifier: Modifier = Modifier,
    theme: SyntaxTheme = SyntaxThemes.pastel(),
) = Row(modifier = modifier) {
    val codeVerticalScrollState = rememberScrollState()
    val codeHighlights by remember(theme, code) {
        derivedStateOf {
            Highlights.Builder()
                .code(code)
                .theme(theme)
                .language(SyntaxLanguage.KOTLIN)
                .build()
        }
    }
    SelectionContainer(Modifier.fillMaxSize()) {
        Text(
            text = buildAnnotatedString {
                text(codeHighlights.getCode())
                codeHighlights.getHighlights()
                    .filterIsInstance<ColorHighlight>()
                    .forEach {
                        addStyle(
                            SpanStyle(color = Color(it.rgb).copy(alpha = 1f)),
                            start = it.location.start,
                            end = it.location.end,
                        )
                    }
                codeHighlights.getHighlights()
                    .filterIsInstance<BoldHighlight>()
                    .forEach {
                        addStyle(
                            SpanStyle(fontWeight = FontWeight.Bold),
                            start = it.location.start,
                            end = it.location.end,
                        )
                    }
            },
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontFamily = FontFamily(Font(Res.font.JetBrainsMono_Regular)),
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(rememberScrollState())
                .verticalScroll(codeVerticalScrollState)
                .padding(12.dp),
        )
    }
}

val JetBrainsMonoRegularRes = Res.font.JetBrainsMono_Regular
