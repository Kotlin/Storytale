import androidx.compose.compiler.plugins.kotlin.ComposePluginRegistrar
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.sourcesGeneratedBySymbolProcessor
import com.tschuchort.compiletesting.symbolProcessorProviders
import com.tschuchort.compiletesting.useKsp2
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.util.Files
import org.jetbrains.kotlin.utils.fileUtils.descendantRelativeTo
import org.junit.Test
import util.hasContent

class PreviewProcessorTest {
    @Test
    fun `generates story for Jetbrains Compose Preview function`() {
        val group1Kt = SourceFile.kotlin(
            "KmpButton.kt",
            """
                package storytale.gallery.demo

                @androidx.compose.runtime.Composable
                fun KmpButton() { }

                @org.jetbrains.compose.ui.tooling.preview.Preview
                @androidx.compose.runtime.Composable
                fun PreviewKmpButton() {
                    KmpButton()
                }
    """,
        )
        val group2Kt = SourceFile.kotlin(
            "AndroidButton.kt",
            """package storytale.gallery.demo

                @androidx.compose.runtime.Composable
                fun AndroidButton() { }

                @org.jetbrains.compose.ui.tooling.preview.Preview
                @androidx.compose.runtime.Composable
                fun PreviewAndroidButton() {
                    AndroidButton()
                }
    """,
        )

        val compilation = KotlinCompilation().apply {
            sources = listOf(group1Kt)

            compilerPluginRegistrars = listOf(ComposePluginRegistrar())
            useKsp2()
            symbolProcessorProviders.add(PreviewProcessor.Provider())

            // magic
            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
            jvmTarget = "21"
            verbose = false
        }
        val result = compilation
            .compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        assertThat(result.sourcesGeneratedBySymbolProcessor.toList()).hasSize(1)

        assertThat(
            result.sourcesGeneratedBySymbolProcessor.toList().map {
                it.descendantRelativeTo(compilation.kspSourcesDir).path to
                    Files.contentOf(it, Charsets.UTF_8).trim()
            },
        )
            .containsExactlyInAnyOrder(
                "kotlin/org/jetbrains/compose/storytale/generated/Previews.story.kt" hasContent """
                |package org.jetbrains.compose.storytale.generated
                |
                |import org.jetbrains.compose.storytale.Story
                |import org.jetbrains.compose.storytale.story
                |import storytale.gallery.demo.PreviewKmpButton
                |
                |public val KmpButton: Story by story {
                |    PreviewKmpButton()
                |}
                """.trimMargin(),
            )
    }

    @Test
    fun `generates story for Androidx Desktop Preview function`() {
        val sourceFile = SourceFile.kotlin(
            "DesktopButton.kt",
            """
                package storytale.gallery.demo

                @androidx.compose.runtime.Composable
                fun DesktopButton() { }

                @androidx.compose.desktop.ui.tooling.preview.Preview
                @androidx.compose.runtime.Composable
                fun PreviewDesktopButton() {
                    DesktopButton()
                }
            """,
        )

        val compilation = KotlinCompilation().apply {
            sources = listOf(sourceFile)

            compilerPluginRegistrars = listOf(ComposePluginRegistrar())
            useKsp2()
            symbolProcessorProviders.add(PreviewProcessor.Provider())

            // magic
            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
            jvmTarget = "21"
            verbose = false
        }
        val result = compilation
            .compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        assertThat(result.sourcesGeneratedBySymbolProcessor.toList()).hasSize(1)

        assertThat(
            result.sourcesGeneratedBySymbolProcessor.toList().map {
                it.descendantRelativeTo(compilation.kspSourcesDir).path to
                    Files.contentOf(it, Charsets.UTF_8).trim()
            },
        )
            .containsExactlyInAnyOrder(
                "kotlin/org/jetbrains/compose/storytale/generated/Previews.story.kt" hasContent """
                |package org.jetbrains.compose.storytale.generated
                |
                |import org.jetbrains.compose.storytale.Story
                |import org.jetbrains.compose.storytale.story
                |import storytale.gallery.demo.PreviewDesktopButton
                |
                |public val DesktopButton: Story by story {
                |    PreviewDesktopButton()
                |}
                """.trimMargin(),
            )
    }

}
