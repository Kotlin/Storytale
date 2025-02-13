package org.jetbrains.dokka.storytale

import kotlinx.html.*
import org.jetbrains.dokka.base.renderers.html.HtmlRenderer
import org.jetbrains.dokka.model.doc.P
import org.jetbrains.dokka.model.doc.Text
import org.jetbrains.dokka.pages.*
import org.jetbrains.dokka.plugability.DokkaContext
import java.util.*

const val STORY_IFRAME_CODE = "@story/iframe"

open class StoryHtmlRenderer(context: DokkaContext) : HtmlRenderer(context) {
    override fun FlowContent.buildCodeBlock(code: ContentCodeBlock, pageContext: ContentPage) {
        if (code.language == STORY_IFRAME_CODE) {
            iframe {
                width = "450"
                height = "450"
                style = "width: 450px; height: 450px; display: flex; margin: 15px auto;"
                src = (code.children.single() as ContentText).text
                attributes["allow"] = "clipboard-read; clipboard-write"
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
                fun FlowContent.copiedPopup(notificationContent: String, additionalClasses: String = "") =
                    div("copy-popup-wrapper $additionalClasses") {
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