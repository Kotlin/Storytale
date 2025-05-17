import com.tschuchort.compiletesting.KotlinCompilation
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import util.storytaleTest
import java.lang.reflect.Modifier

class PublicPreviewComposableRegistrarTest {

    @Test
    fun `ensure that private Preview functions are made public`() {
        val compilation = storytaleTest {
            "PrivatePreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable
                import org.jetbrains.compose.ui.tooling.preview.Preview

                @Composable
                private fun PrivateComponent() {}

                @Preview
                @Composable
                private fun PrivatePreviewFunction() {
                    PrivateComponent()
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        // Load the compiled class
        val classLoader = result.classLoader
        val previewClass = classLoader.loadClass("test.PrivatePreviewKt")

        // Check that the preview function is public
        val previewFunction = previewClass.declaredMethods.find { it.name == "PrivatePreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()

        // Regular private function should remain private
        val privateComponent = previewClass.declaredMethods.find { it.name == "PrivateComponent" }
        assertThat(privateComponent).isNotNull
        assertThat(Modifier.isPrivate(privateComponent!!.modifiers)).isTrue()
    }

    @Test
    fun `ensure that internal Preview functions are made public`() {
        val compilation = storytaleTest {
            "InternalPreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable
                import org.jetbrains.compose.ui.tooling.preview.Preview

                @Composable
                internal fun InternalComponent() {}

                @Preview
                @Composable
                internal fun InternalPreviewFunction() {
                    InternalComponent()
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        // Load the compiled class
        val classLoader = result.classLoader
        val previewClass = classLoader.loadClass("test.InternalPreviewKt")

        // Check that the preview function is public
        val previewFunction = previewClass.declaredMethods.find { it.name == "InternalPreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()

        // Internal component should remain internal
        val internalComponent = previewClass.declaredMethods.find { it.name == "InternalComponent" }
        assertThat(internalComponent).isNotNull
        assertThat(!Modifier.isPrivate(internalComponent!!.modifiers) && !Modifier.isPublic(internalComponent.modifiers)).isTrue()
    }

    @Test
    fun `ensure that protected Preview functions are made public in classes`() {
        val compilation = storytaleTest {
            "ProtectedPreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable
                import org.jetbrains.compose.ui.tooling.preview.Preview

                open class PreviewContainer {
                    @Composable
                    protected fun ProtectedComponent() {}

                    @Preview
                    @Composable
                    protected fun ProtectedPreviewFunction() {
                        ProtectedComponent()
                    }
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        // Load the compiled class
        val classLoader = result.classLoader
        val containerClass = classLoader.loadClass("test.PreviewContainer")

        // Check that the preview function is public
        val previewFunction = containerClass.declaredMethods.find { it.name == "ProtectedPreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()

        // Protected component should remain protected
        val protectedComponent = containerClass.declaredMethods.find { it.name == "ProtectedComponent" }
        assertThat(protectedComponent).isNotNull
        assertThat(Modifier.isProtected(protectedComponent!!.modifiers)).isTrue()
    }

    @Test
    fun `ensure that public Preview functions remain public`() {
        val compilation = storytaleTest {
            "PublicPreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable
                import org.jetbrains.compose.ui.tooling.preview.Preview

                @Composable
                fun PublicComponent() {}

                @Preview
                @Composable
                fun PublicPreviewFunction() {
                    PublicComponent()
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        // Load the compiled class
        val classLoader = result.classLoader
        val previewClass = classLoader.loadClass("test.PublicPreviewKt")

        // Check that the preview function is public
        val previewFunction = previewClass.declaredMethods.find { it.name == "PublicPreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()
    }
}
