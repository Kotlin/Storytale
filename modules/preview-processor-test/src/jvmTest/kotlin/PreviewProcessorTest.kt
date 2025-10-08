import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.sourcesGeneratedBySymbolProcessor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import util.assertableGeneratedKspSources
import util.hasContent
import util.storytaleTest

class PreviewProcessorTest {
    @Test
    fun `generates story for Jetbrains Compose Preview function`() {
        val compilation = storytaleTest {
            "KmpButton.kt" hasContent """
                package storytale.gallery.demo

                @androidx.compose.runtime.Composable
                fun KmpButton() { }

                @org.jetbrains.compose.ui.tooling.preview.Preview
                @androidx.compose.runtime.Composable
                fun PreviewKmpButton() {
                    KmpButton()
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
        val compilation = storytaleTest {
            "DesktopButton.kt" hasContent """
                package storytale.gallery.demo

                @androidx.compose.runtime.Composable
                fun DesktopButton() { }

                @androidx.compose.desktop.ui.tooling.preview.Preview
                @androidx.compose.runtime.Composable
                fun PreviewDesktopButton() {
                    DesktopButton()
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
                |import storytale.gallery.demo.PreviewDesktopButton
                |
                |public val DesktopButton: Story by story {
                |    PreviewDesktopButton()
                |}
                """.trimMargin(),
            )
    }

    @Test
    fun `generates stories for multiple Preview functions in the same file`() {
        val compilation = storytaleTest {
            "MultiplePreviews.kt" hasContent """
                package storytale.gallery.demo

                import androidx.compose.runtime.Composable
                import org.jetbrains.compose.ui.tooling.preview.Preview

                @Composable
                fun Button() { }

                @Preview
                @Composable
                fun PreviewButton1() {
                    Button()
                }
                @Preview
                @Composable
                fun PreviewButton2() {
                    Button()
                }
            """.trimIndent()
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        assertThat(result.sourcesGeneratedBySymbolProcessor.toList()).hasSize(1)

        assertThat(result.assertableGeneratedKspSources(compilation))
            .containsExactlyInAnyOrder(
                "kotlin/org/jetbrains/compose/storytale/generated/Previews.story.kt" hasContent """
                |package org.jetbrains.compose.storytale.generated
                |
                |import org.jetbrains.compose.storytale.Story
                |import org.jetbrains.compose.storytale.story
                |import storytale.gallery.demo.PreviewButton1
                |import storytale.gallery.demo.PreviewButton2
                |
                |public val Button1: Story by story {
                |    PreviewButton1()
                |}
                |
                |public val Button2: Story by story {
                |    PreviewButton2()
                |}
                """.trimMargin(),
            )
    }

    @Test
    fun `generates stories for Preview functions in different files calling same component`() {
        val compilation = storytaleTest {
            "CommonButton.kt" hasContent """
                package storytale.gallery.demo

                import androidx.compose.runtime.Composable

                @Composable
                fun Button() { }
            """.trimIndent()

            "FirstPreview.kt" hasContent """
                package storytale.gallery.demo.first

                import androidx.compose.runtime.Composable
                import org.jetbrains.compose.ui.tooling.preview.Preview
                import storytale.gallery.demo.Button

                @Preview
                @Composable
                fun PreviewButtonPrimary() {
                    Button()
                }
            """.trimIndent()

            "SecondPreview.kt" hasContent """
                package storytale.gallery.demo.second

                import androidx.compose.runtime.Composable
                import org.jetbrains.compose.ui.tooling.preview.Preview
                import storytale.gallery.demo.Button

                @Preview
                @Composable
                fun PreviewButtonSecondary() {
                    Button()
                }
            """.trimIndent()
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        assertThat(result.sourcesGeneratedBySymbolProcessor.toList()).hasSize(1)

        assertThat(result.assertableGeneratedKspSources(compilation))
            .containsExactlyInAnyOrder(
                "kotlin/org/jetbrains/compose/storytale/generated/Previews.story.kt" hasContent """
                |package org.jetbrains.compose.storytale.generated
                |
                |import org.jetbrains.compose.storytale.Story
                |import org.jetbrains.compose.storytale.story
                |import storytale.gallery.demo.first.PreviewButtonPrimary
                |import storytale.gallery.demo.second.PreviewButtonSecondary
                |
                |public val ButtonPrimary: Story by story {
                |    PreviewButtonPrimary()
                |}
                |
                |public val ButtonSecondary: Story by story {
                |    PreviewButtonSecondary()
                |}
                """.trimMargin(),
            )
    }

    @Test
    fun `verifies stories are generated in alphabetical order`() {
        val compilation = storytaleTest {
            "ZAButton.kt" hasContent """
                package storytale.gallery.demo.z

                import androidx.compose.runtime.Composable
                import org.jetbrains.compose.ui.tooling.preview.Preview

                @Composable
                fun ZAButton() { }

                @Preview
                @Composable
                fun PreviewZAButton() {
                    ZAButton()
                }
            """.trimIndent()

            "BButton.kt" hasContent """
                package storytale.gallery.demo.b

                import androidx.compose.runtime.Composable
                import org.jetbrains.compose.ui.tooling.preview.Preview

                @Composable
                fun BButton() { }

                @Preview
                @Composable
                fun PreviewBButton() {
                    BButton()
                }
            """.trimIndent()

            "AButton.kt" hasContent """
                package storytale.gallery.demo.a

                import androidx.compose.runtime.Composable
                import org.jetbrains.compose.ui.tooling.preview.Preview

                @Composable
                fun AButton() { }

                @Preview
                @Composable
                fun PreviewAButton() {
                    AButton()
                }
            """.trimIndent()
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        assertThat(result.sourcesGeneratedBySymbolProcessor.toList()).hasSize(1)

        // Verify the stories are generated in alphabetical order based on qualified name
        // The package name affects the sorting order: a.PreviewAButton comes before b.PreviewBButton before z.PreviewZAButton
        val generatedSource = result.assertableGeneratedKspSources(compilation).first()
        val content = generatedSource.content

        val importLines = content.lines().filter { it.startsWith("import storytale.gallery.demo") }
        assertThat(importLines).containsExactly(
            "import storytale.gallery.demo.a.PreviewAButton",
            "import storytale.gallery.demo.b.PreviewBButton",
            "import storytale.gallery.demo.z.PreviewZAButton",
        )

        val storyLines = content.lines().filter { it.startsWith("public val") }
        assertThat(storyLines).containsExactly(
            "public val AButton: Story by story {",
            "public val BButton: Story by story {",
            "public val ZAButton: Story by story {",
        )
    }
}
