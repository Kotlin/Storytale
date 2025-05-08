import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import com.tschuchort.compiletesting.useKsp2
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PreviewProcessorTest {
    @Test
    fun `first good test`() {
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

                @androidx.compose.ui.tooling.preview.Preview
                @androidx.compose.runtime.Composable
                fun PreviewAndroidButton() {
                    AndroidButton()
                }
    """,
        )

        val result = KotlinCompilation().apply {
            sources = listOf(group1Kt)

            useKsp2()
            symbolProcessorProviders.add(PreviewProcessor.Provider())// = mutableListOf(PreviewProcessor.Provider())

            // magic
            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
            jvmTarget = "21"
            verbose = false
        }
            .compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
    }
}
