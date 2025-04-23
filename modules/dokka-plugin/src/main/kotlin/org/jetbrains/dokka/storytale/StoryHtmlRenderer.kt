package org.jetbrains.dokka.storytale

import java.util.Locale
import kotlinx.html.FlowContent
import kotlinx.html.code
import kotlinx.html.div
import kotlinx.html.iframe
import kotlinx.html.pre
import kotlinx.html.span
import kotlinx.html.style
import org.jetbrains.dokka.base.renderers.html.HtmlRenderer
import org.jetbrains.dokka.pages.ContentCodeBlock
import org.jetbrains.dokka.pages.ContentPage
import org.jetbrains.dokka.pages.ContentStyle
import org.jetbrains.dokka.pages.ContentText
import org.jetbrains.dokka.pages.TextStyle
import org.jetbrains.dokka.plugability.DokkaContext

const val STORY_IFRAME_CODE = "@story/iframe"

open class StoryHtmlRenderer(context: DokkaContext) : HtmlRenderer(context) {
    override fun FlowContent.buildCodeBlock(code: ContentCodeBlock, pageContext: ContentPage) {
        if (code.language == STORY_IFRAME_CODE) {
            div(classes = "storytale-embedded-container") {
                iframe(classes = "storytale-embedded") {
                    style = "display: flex; margin: 15px auto;"
                    src = (code.children.single() as ContentText).text
                    attributes["allow"] = "clipboard-read; clipboard-write"
                }
            }
        } else {
            div("sample-container") {
                val codeLang = "lang-" + code.language.ifEmpty { "kotlin" }
                val stylesWithBlock = code.style + TextStyle.Block + codeLang
                pre {
                    code(stylesWithBlock.joinToString(" ") { it.toString().lowercase(Locale.getDefault()) }) {
                        attributes["theme"] = "idea"
                        code.children.forEach { buildContentNode(it, pageContext) }
                    }
                }
                fun FlowContent.copiedPopup(
                    notificationContent: String,
                    additionalClasses: String = "",
                ) = div("copy-popup-wrapper $additionalClasses") {
                    span("copy-popup-icon")
                    span {
                        text(notificationContent)
                    }
                }

                fun FlowContent.copyButton() = span(classes = "top-right-position") {
                    span("copy-icon")
                    copiedPopup("Content copied to clipboard", "popup-to-left")
                }
                /*
                Disable copy button on samples as:
                 - it is useless
                 - it overflows with playground's run button
                 */
                if (!code.style.contains(ContentStyle.RunnableSample)) copyButton()
            }
        }
    }
}
