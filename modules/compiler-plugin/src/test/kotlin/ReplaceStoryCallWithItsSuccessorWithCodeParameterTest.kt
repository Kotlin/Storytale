import com.tschuchort.compiletesting.KotlinCompilation
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.compose.storytale.storiesStorage
import org.junit.Test
import util.invokeMainController
import util.storytaleTest

class ReplaceStoryCallWithItsSuccessorWithCodeParameterTest {
    @Test
    fun `replace story call extracts code parameter`() {
        val result = storytaleTest {
            "Group1.story.kt" hasContent """
                package storytale.gallery.demo
                import org.jetbrains.compose.storytale.story
                import androidx.compose.foundation.text.BasicText

                val Story1 by story {
                    val text by parameter("Hello World")
                    BasicText(text)
                }
            """
            "Main.kt" hasContent """
                package org.jetbrains.compose.storytale.generated
                fun MainViewController() { }
            """
        }

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        result.invokeMainController()

        assertThat(storiesStorage).hasSize(1)
        with(storiesStorage.first()) {
            assertThat(name).isEqualTo("Story1")
            assertThat(group).isEqualTo("Group1")
            assertThat(code).isEqualTo(
                """
                    |val text by parameter("Hello World")
                    |BasicText(text)
                """
                    .trimMargin()
                    .trim('\n'),
            )
        }
    }
}
