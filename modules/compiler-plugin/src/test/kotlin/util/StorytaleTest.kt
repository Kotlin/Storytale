package util

import androidx.compose.compiler.plugins.kotlin.ComposePluginRegistrar
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import org.assertj.core.api.Assertions
import org.intellij.lang.annotations.Language
import org.jetbrains.compose.plugin.storytale.compiler.StorytaleComponentRegistrar
import org.jetbrains.compose.storytale.storiesStorage

@OptIn(ExperimentalContracts::class)
fun storytaleTest(
    compilationBuilder: KotlinCompilation.() -> Unit = {},
    testSourceBuilder: StorytaleTestSourceScope.() -> Unit,
): JvmCompilationResult {
    contract {
        callsInPlace(compilationBuilder, InvocationKind.EXACTLY_ONCE)
        callsInPlace(testSourceBuilder, InvocationKind.EXACTLY_ONCE)
    }
    storiesStorage.clear()

    val testSourceScope = object : StorytaleTestSourceScope {
        val sources = mutableListOf<SourceFile>()
        override fun String.hasContent(content: String) {
            sources.add(SourceFile.Companion.kotlin(this, content))
        }
    }

    val compilationSetup = KotlinCompilation().apply {
        compilerPluginRegistrars =
            listOf(StorytaleComponentRegistrar(), ComposePluginRegistrar())
        inheritClassPath = true
        messageOutputStream = System.out // see diagnostics in real time
        jvmTarget = "21"
        verbose = false
    }

    compilationSetup.compilationBuilder()
    testSourceScope.testSourceBuilder()

    compilationSetup.sources = testSourceScope.sources

    return compilationSetup.compile()
}

interface StorytaleTestSourceScope {
    infix fun String.hasContent(@Language("kotlin") content: String)
}

fun JvmCompilationResult.invokeMainController() {
    val clazz = classLoader.loadClass("org.jetbrains.compose.storytale.generated.MainKt")

    Assertions.assertThat(clazz).isNotNull
    Assertions.assertThat(clazz).hasDeclaredMethods("MainViewController")

    clazz.getMethod("MainViewController").invoke(null)
}
