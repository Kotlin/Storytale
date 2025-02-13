package org.jetbrains.dokka.storytale

import org.jetbrains.dokka.base.testApi.testRunner.BaseAbstractTest
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import utils.TestOutputWriterPlugin
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StorytalePluginTest : BaseAbstractTest() {
    private val configuration = dokkaConfiguration {
        sourceSets {
            sourceSet {
                sourceRoots = listOf("src/main/kotlin")
            }
        }
    }

    @Test
    fun `test iframe generation with story tag`() {
        val writerPlugin = TestOutputWriterPlugin()
        val source = """
            |/src/main/kotlin/test/Test.kt
            |package example
            |
            |/**
            | * Function with story tag
            | * @story https://storytale.io/stories/12345
            | */
            |fun testWithStory(): String = "Has story"
            """.trimIndent()

        testInline(
            source,
            configuration,
            pluginOverrides = listOf(writerPlugin, StorytalePlugin())
        ) {
            renderingStage = { _, _ ->
                val html = writerPlugin.writer.contents.getValue("root/example/test-with-story.html")
                val doc = Jsoup.parse(html)
                val iframes = doc.select("iframe")

                assertTrue(iframes.isNotEmpty(), "Should have an iframe")
                assertEquals(
                    "https://storytale.io/stories/12345",
                    iframes.first()?.attr("src"),
                    "Iframe should have correct URL"
                )
            }
        }
    }

    @Test
    fun `test no iframe without story tag`() {
        val writerPlugin = TestOutputWriterPlugin()
        val source = """
            |/src/main/kotlin/test/Test.kt
            |package example
            |
            |/**
            | * Regular function without story tag
            | */
            |fun testWithoutStory(): String = "No story"
            """.trimIndent()

        testInline(
            source,
            configuration,
            pluginOverrides = listOf(writerPlugin, StorytalePlugin())
        ) {
            renderingStage = { _, _ ->
                val html = writerPlugin.writer.contents.getValue("root/example/test-without-story.html")
                val doc = Jsoup.parse(html)
                val iframes = doc.select("iframe")

                assertTrue(iframes.isEmpty(), "Should not have any iframes")
            }
        }
    }
}
