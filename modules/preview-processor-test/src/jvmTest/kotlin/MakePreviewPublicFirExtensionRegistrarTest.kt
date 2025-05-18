import com.tschuchort.compiletesting.KotlinCompilation
import java.lang.reflect.Modifier
import kotlin.test.Ignore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import util.storytaleTest

class MakePreviewPublicFirExtensionRegistrarTest {

    @Test
    fun `ensure that private Jetbrains Compose Preview functions are made public`() {
        val compilation = storytaleTest {
            "PrivatePreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable
                import org.jetbrains.compose.ui.tooling.preview.Preview

                @Composable
                private fun PrivateComponent() {}

                @org.jetbrains.compose.ui.tooling.preview.Preview
                @Composable
                private fun PrivatePreviewFunction() {
                    PrivateComponent()
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val classLoader = result.classLoader
        val previewClass = classLoader.loadClass("test.PrivatePreviewKt")

        val previewFunction = previewClass.declaredMethods.find { it.name == "PrivatePreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()

        val privateComponent = previewClass.declaredMethods.find { it.name == "PrivateComponent" }
        assertThat(privateComponent).isNotNull
        assertThat(Modifier.isPrivate(privateComponent!!.modifiers)).isTrue()
    }

    @Test
    fun `ensure that internal Jetbrains Compose Preview functions are made public`() {
        val compilation = storytaleTest {
            "InternalPreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable

                @Composable
                internal fun InternalComponent() {}

                @org.jetbrains.compose.ui.tooling.preview.Preview
                @Composable
                internal fun InternalPreviewFunction() {
                    InternalComponent()
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val classLoader = result.classLoader
        val previewClass = classLoader.loadClass("test.InternalPreviewKt")

        val previewFunction = previewClass.declaredMethods.find { it.name == "InternalPreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()

        val internalComponent = previewClass.declaredMethods.find { it.name == "InternalComponent" }
        assertThat(internalComponent).isNotNull
        assertThat(Modifier.isPublic(internalComponent!!.modifiers)).isTrue()
    }

    @Ignore("PreviewProcessor hasn't support protected Preview functions in classes yet")
    @Test
    fun `ensure that protected Jetbrains Compose Preview functions are made public in classes`() {
        val compilation = storytaleTest {
            "ProtectedPreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable

                open class PreviewContainer {
                    @Composable
                    protected fun ProtectedComponent() {}

                    @org.jetbrains.compose.ui.tooling.preview.Preview
                    @Composable
                    protected fun ProtectedPreviewFunction() {
                        ProtectedComponent()
                    }
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val classLoader = result.classLoader
        val containerClass = classLoader.loadClass("test.PreviewContainer")

        val previewFunction = containerClass.declaredMethods.find { it.name == "ProtectedPreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()

        val protectedComponent = containerClass.declaredMethods.find { it.name == "ProtectedComponent" }
        assertThat(protectedComponent).isNotNull
        assertThat(Modifier.isPublic(protectedComponent!!.modifiers)).isTrue()
    }

    @Test
    fun `ensure that public Jetbrains Compose Preview functions remain public`() {
        val compilation = storytaleTest {
            "PublicPreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable

                @Composable
                fun PublicComponent() {}

                @org.jetbrains.compose.ui.tooling.preview.Preview
                @Composable
                fun PublicPreviewFunction() {
                    PublicComponent()
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val classLoader = result.classLoader
        val previewClass = classLoader.loadClass("test.PublicPreviewKt")

        val previewFunction = previewClass.declaredMethods.find { it.name == "PublicPreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()
    }

    @Test
    fun `ensure that private androidx desktop Preview functions are made public`() {
        val compilation = storytaleTest {
            "PrivateDesktopPreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable

                @Composable
                private fun PrivateDesktopComponent() {}

                @androidx.compose.desktop.ui.tooling.preview.Preview
                @Composable
                private fun PrivateDesktopPreviewFunction() {
                    PrivateDesktopComponent()
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val classLoader = result.classLoader
        val previewClass = classLoader.loadClass("test.PrivateDesktopPreviewKt")

        val previewFunction = previewClass.declaredMethods.find { it.name == "PrivateDesktopPreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()

        val privateComponent = previewClass.declaredMethods.find { it.name == "PrivateDesktopComponent" }
        assertThat(privateComponent).isNotNull
        assertThat(Modifier.isPrivate(privateComponent!!.modifiers)).isTrue()
    }

    @Test
    fun `ensure that internal androidx desktop Preview functions are made public`() {
        val compilation = storytaleTest {
            "InternalDesktopPreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable

                @Composable
                internal fun InternalDesktopComponent() {}

                @androidx.compose.desktop.ui.tooling.preview.Preview
                @Composable
                internal fun InternalDesktopPreviewFunction() {
                    InternalDesktopComponent()
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val classLoader = result.classLoader
        val previewClass = classLoader.loadClass("test.InternalDesktopPreviewKt")

        val previewFunction = previewClass.declaredMethods.find { it.name == "InternalDesktopPreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()

        val internalComponent = previewClass.declaredMethods.find { it.name == "InternalDesktopComponent" }
        assertThat(internalComponent).isNotNull
        assertThat(Modifier.isPublic(internalComponent!!.modifiers)).isTrue()
    }

    @Ignore("PreviewProcessor hasn't support protected Preview functions in classes yet")
    @Test
    fun `ensure that protected androidx desktop Preview functions are made public in classes`() {
        val compilation = storytaleTest {
            "ProtectedDesktopPreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable

                open class PreviewDesktopContainer {
                    @Composable
                    protected fun ProtectedDesktopComponent() {}

                    @androidx.compose.desktop.ui.tooling.preview.Preview
                    @Composable
                    protected fun ProtectedDesktopPreviewFunction() {
                        ProtectedDesktopComponent()
                    }
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val classLoader = result.classLoader
        val containerClass = classLoader.loadClass("test.PreviewDesktopContainer")

        val previewFunction = containerClass.declaredMethods.find { it.name == "ProtectedDesktopPreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()

        val protectedComponent = containerClass.declaredMethods.find { it.name == "ProtectedDesktopComponent" }
        assertThat(protectedComponent).isNotNull
        assertThat(Modifier.isPublic(protectedComponent!!.modifiers)).isTrue()
    }

    @Test
    fun `ensure that public androidx desktop Preview functions remain public`() {
        val compilation = storytaleTest {
            "PublicDesktopPreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable

                @Composable
                fun PublicDesktopComponent() {}

                @androidx.compose.desktop.ui.tooling.preview.Preview
                @Composable
                fun PublicDesktopPreviewFunction() {
                    PublicDesktopComponent()
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val classLoader = result.classLoader
        val previewClass = classLoader.loadClass("test.PublicDesktopPreviewKt")

        val previewFunction = previewClass.declaredMethods.find { it.name == "PublicDesktopPreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()
    }
}
