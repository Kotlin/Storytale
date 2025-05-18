package util

import PreviewProcessor
import PreviewComponentRegistrar
import androidx.compose.compiler.plugins.kotlin.ComposePluginRegistrar
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.sourcesGeneratedBySymbolProcessor
import com.tschuchort.compiletesting.symbolProcessorProviders
import com.tschuchort.compiletesting.useKsp2
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.utils.fileUtils.descendantRelativeTo

@OptIn(ExperimentalContracts::class)
fun storytaleTest(
    compilationBuilder: KotlinCompilation.() -> Unit = {},
    testSourceBuilder: StorytaleTestSourceScope.() -> Unit,
): KotlinCompilation {
    contract {
        callsInPlace(compilationBuilder, InvocationKind.EXACTLY_ONCE)
    }

    val testSourceScope = object : StorytaleTestSourceScope {
        val sources = mutableListOf<SourceFile>()
        override fun String.hasContent(content: String) {
            sources.add(SourceFile.Companion.kotlin(this, content))
        }
    }

    return KotlinCompilation().apply {
        compilerPluginRegistrars = listOf(
            ComposePluginRegistrar(),
            PreviewComponentRegistrar(),
        )
        useKsp2()
        symbolProcessorProviders.add(PreviewProcessor.Provider())

        // magic
        inheritClassPath = true
        messageOutputStream = System.out // see diagnostics in real time
        jvmTarget = "21"
        verbose = false

        compilationBuilder()
        testSourceScope.testSourceBuilder()
        sources = testSourceScope.sources
    }
}

interface StorytaleTestSourceScope {
    infix fun String.hasContent(@Language("kotlin") content: String)
}

fun JvmCompilationResult.assertableGeneratedKspSources(
    compilation: KotlinCompilation,
): List<AssertableFile> {
    return sourcesGeneratedBySymbolProcessor.toList().map {
        it.descendantRelativeTo(compilation.kspSourcesDir).path hasContent
            org.assertj.core.util.Files.contentOf(it, Charsets.UTF_8).trim()
    }
}
