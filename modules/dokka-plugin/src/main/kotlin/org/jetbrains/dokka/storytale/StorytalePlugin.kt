/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package  org.jetbrains.dokka.storytale


import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.transformers.pages.tags.CustomTagContentProvider
import org.jetbrains.dokka.base.translators.documentables.PageContentBuilder.DocumentableContentBuilder
import org.jetbrains.dokka.model.doc.CustomTagWrapper
import org.jetbrains.dokka.model.doc.Text
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.plugability.DokkaPluginApiPreview
import org.jetbrains.dokka.plugability.PluginApiPreviewAcknowledgement

public class StorytalePlugin : DokkaPlugin() {
    private val dokkaBase by lazy { plugin<DokkaBase>() }

    public val storytaleRenderer by extending {
        CoreExtensions.renderer providing { StoryHtmlRenderer(it) } override dokkaBase.htmlRenderer
    }

    public val storyTagContentProvider by extending {
        plugin<DokkaBase>().customTagContentProvider with StoryTagContentProvider order {
            before(plugin<DokkaBase>().sinceKotlinTagContentProvider)
        }
    }

    @OptIn(DokkaPluginApiPreview::class)
    override fun pluginApiPreviewAcknowledgement(): PluginApiPreviewAcknowledgement =
        PluginApiPreviewAcknowledgement
}

private const val STORY_TAG = "story"

private object StoryTagContentProvider : CustomTagContentProvider {
    override fun isApplicable(customTag: CustomTagWrapper): Boolean = 
        customTag.name == STORY_TAG

    override fun DocumentableContentBuilder.contentForDescription(
        sourceSet: DokkaConfiguration.DokkaSourceSet,
        customTag: CustomTagWrapper
    ) {
        val url = customTag.extractUrl() ?: return

        codeBlock(language = STORY_IFRAME_CODE) { text(url) }
    }

    private fun CustomTagWrapper.extractUrl(): String? =
        (((root.children.firstOrNull() as? org.jetbrains.dokka.model.doc.P)?.children as? ArrayList)?.singleOrNull() as? Text)?.body
}
