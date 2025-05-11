import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.sourcesGeneratedBySymbolProcessor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import util.assertableGeneratedKspSources
import util.hasContent
import util.storytaleTest

class PreviewProcessorAndroidTest {

    @Test
    fun `generates story for Androidx Android Preview function`() {
        val compilation = storytaleTest {
            "AndroidButton.kt" hasContent """
                package storytale.gallery.demo

                @androidx.compose.runtime.Composable
                fun AndroidButton() { }

                @androidx.compose.ui.tooling.preview.Preview
                @androidx.compose.runtime.Composable
                fun PreviewAndroidButton() {
                    AndroidButton()
                }
            """
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
                |import storytale.gallery.demo.PreviewAndroidButton
                |
                |public val AndroidButton: Story by story {
                |    PreviewAndroidButton()
                |}
                """.trimMargin(),
            )
    }
}
