package com.storytale

import com.tschuchort.compiletesting.KotlinCompilation
import java.lang.reflect.Modifier
import kotlin.test.Ignore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import util.storytaleTest

class MakePreviewPublicFirExtensionRegistrarAndroidTest {

    @Test
    fun `ensure that private androidx Preview functions are made public`() {
        val compilation = storytaleTest {
            "PrivateAndroidPreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable

                @Composable
                private fun PrivateAndroidComponent() {}

                @androidx.compose.ui.tooling.preview.Preview
                @Composable
                private fun PrivateAndroidPreviewFunction() {
                    PrivateAndroidComponent()
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val classLoader = result.classLoader
        val previewClass = classLoader.loadClass("test.PrivateAndroidPreviewKt")

        val previewFunction = previewClass.declaredMethods.find { it.name == "PrivateAndroidPreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()

        val privateComponent = previewClass.declaredMethods.find { it.name == "PrivateAndroidComponent" }
        assertThat(privateComponent).isNotNull
        assertThat(Modifier.isPrivate(privateComponent!!.modifiers)).isTrue()
    }

    @Test
    fun `ensure that internal androidx Preview functions are made public`() {
        val compilation = storytaleTest {
            "InternalAndroidPreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable

                @Composable
                internal fun InternalAndroidComponent() {}

                @androidx.compose.ui.tooling.preview.Preview
                @Composable
                internal fun InternalAndroidPreviewFunction() {
                    InternalAndroidComponent()
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val classLoader = result.classLoader
        val previewClass = classLoader.loadClass("test.InternalAndroidPreviewKt")

        val previewFunction = previewClass.declaredMethods.find { it.name == "InternalAndroidPreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()

        val internalComponent = previewClass.declaredMethods.find { it.name == "InternalAndroidComponent" }
        assertThat(internalComponent).isNotNull
        assertThat(Modifier.isPublic(internalComponent!!.modifiers)).isTrue()
    }

    @Ignore("PreviewProcessor hasn't support protected Preview functions in classes yet")
    @Test
    fun `ensure that protected androidx Preview functions are made public in classes`() {
        val compilation = storytaleTest {
            "ProtectedAndroidPreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable

                open class PreviewAndroidContainer {
                    @Composable
                    protected fun ProtectedAndroidComponent() {}

                    @androidx.compose.ui.tooling.preview.Preview
                    @Composable
                    protected fun ProtectedAndroidPreviewFunction() {
                        ProtectedAndroidComponent()
                    }
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val classLoader = result.classLoader
        val containerClass = classLoader.loadClass("test.PreviewAndroidContainer")

        val previewFunction = containerClass.declaredMethods.find { it.name == "ProtectedAndroidPreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()

        val protectedComponent = containerClass.declaredMethods.find { it.name == "ProtectedAndroidComponent" }
        assertThat(protectedComponent).isNotNull
        assertThat(Modifier.isPublic(protectedComponent!!.modifiers)).isTrue()
    }

    @Test
    fun `ensure that public androidx Preview functions remain public`() {
        val compilation = storytaleTest {
            "PublicAndroidPreview.kt" hasContent """
                package test

                import androidx.compose.runtime.Composable

                @Composable
                fun PublicAndroidComponent() {}

                @androidx.compose.ui.tooling.preview.Preview
                @Composable
                fun PublicAndroidPreviewFunction() {
                    PublicAndroidComponent()
                }
            """
        }
        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val classLoader = result.classLoader
        val previewClass = classLoader.loadClass("test.PublicAndroidPreviewKt")

        val previewFunction = previewClass.declaredMethods.find { it.name == "PublicAndroidPreviewFunction" }
        assertThat(previewFunction).isNotNull
        assertThat(Modifier.isPublic(previewFunction!!.modifiers)).isTrue()
    }
}
