import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.sourcesGeneratedBySymbolProcessor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import util.assertableGeneratedKspSources
import util.createCompilation
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

        val compilation = createCompilation {
            sources = listOf(group1Kt)
        }
        val result = compilation
            .compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        assertThat(result.sourcesGeneratedBySymbolProcessor.toList()).hasSize(1)

        assertThat(result.assertableGeneratedKspSources(compilation))
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

        val compilation = createCompilation {
            sources = listOf(sourceFile)
        }
        val result = compilation
            .compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        assertThat(result.sourcesGeneratedBySymbolProcessor.toList()).hasSize(1)

        assertThat(result.assertableGeneratedKspSources(compilation))
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
