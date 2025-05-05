import androidx.compose.compiler.plugins.kotlin.ComposePluginRegistrar
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.compose.plugin.storytale.compiler.StorytaleComponentRegistrar
import org.jetbrains.compose.storytale.storiesStorage
import util.storytaleTest

class MentionAllStoriesGettersInsideMainFunctionLoweringTest {
    /**
     * This test is a learning test for new comers. Please don't over engineer it
     */
    @Test
    fun `all stories are registered after compilation and main invocation`() {
        // reset to initialize state
        storiesStorage.clear()

        val group1Kt = SourceFile.kotlin(
            "Group1.story.kt",
            """
                package storytale.gallery.demo
                import org.jetbrains.compose.storytale.story

                val Story1 by story { }
                val Story2 by story { }
    """,
        )
        val group2Kt = SourceFile.kotlin(
            "Group2.story.kt",
            """
                package storytale.gallery.demo
                import org.jetbrains.compose.storytale.story

                val Story3 by story { }
    """,
        )
        val mainKt = SourceFile.kotlin(
            "Main.kt",
            """
                package org.jetbrains.compose.storytale.generated
                fun MainViewController() { }
    """,
        )

        val result = KotlinCompilation().apply {
            sources = listOf(group1Kt, group2Kt, mainKt)

            compilerPluginRegistrars =
                listOf(StorytaleComponentRegistrar(), ComposePluginRegistrar())
            // magic
            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
            jvmTarget = "21"
            verbose = false
        }
            .compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val clazz = result.classLoader.loadClass("org.jetbrains.compose.storytale.generated.MainKt")

        assertThat(clazz).isNotNull
        assertThat(clazz).hasDeclaredMethods("MainViewController")

        clazz.getMethod("MainViewController").invoke(null)

        // CRAZY!!!
        assertThat(storiesStorage).hasSize(3)
        assertThat(storiesStorage.map { it.name })
            .containsExactlyInAnyOrder("Story1", "Story2", "Story3")
        assertThat(storiesStorage.map { it.group }.toSet())
            .containsExactlyInAnyOrder("Group1", "Group2")
    }

    @Test
    fun `same as above test but over engineered`() {
        val result = storytaleTest {
            "Group1.story.kt" hasContent """
                package storytale.gallery.demo
                import org.jetbrains.compose.storytale.story

                val Story1 by story { }
                val Story2 by story { }
            """
            "Group2.story.kt" hasContent """
                package storytale.gallery.demo
                import org.jetbrains.compose.storytale.story

                val Story3 by story { }
            """
            "Main.kt" hasContent """
                package org.jetbrains.compose.storytale.generated
                fun MainViewController() { }
            """
        }

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val clazz = result.classLoader.loadClass("org.jetbrains.compose.storytale.generated.MainKt")

        assertThat(clazz).isNotNull
        assertThat(clazz).hasDeclaredMethods("MainViewController")

        clazz.getMethod("MainViewController").invoke(null)

        // CRAZY!!!
        assertThat(storiesStorage).hasSize(3)
        assertThat(storiesStorage.map { it.name })
            .containsExactlyInAnyOrder("Story1", "Story2", "Story3")
        assertThat(storiesStorage.map { it.group }.toSet())
            .containsExactlyInAnyOrder("Group1", "Group2")
    }
}
