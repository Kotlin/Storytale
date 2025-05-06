import com.tschuchort.compiletesting.KotlinCompilation
import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.compose.storytale.storiesStorage
import util.invokeMainController
import util.storytaleTest

class MentionAllStoriesGettersInsideMainFunctionLoweringTest {
    @Test
    fun `all stories are registered after compilation and main invocation`() {
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

        result.invokeMainController()

        // CRAZY!!!
        assertThat(storiesStorage).hasSize(3)
        assertThat(storiesStorage.map { it.name })
            .containsExactlyInAnyOrder("Story1", "Story2", "Story3")
        assertThat(storiesStorage.map { it.group }.toSet())
            .containsExactlyInAnyOrder("Group1", "Group2")
    }
}
