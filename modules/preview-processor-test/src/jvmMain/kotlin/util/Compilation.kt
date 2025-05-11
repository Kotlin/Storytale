package util

import PreviewProcessor
import androidx.compose.compiler.plugins.kotlin.ComposePluginRegistrar
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.sourcesGeneratedBySymbolProcessor
import com.tschuchort.compiletesting.symbolProcessorProviders
import com.tschuchort.compiletesting.useKsp2
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import org.jetbrains.kotlin.utils.fileUtils.descendantRelativeTo

@OptIn(ExperimentalContracts::class)
fun createCompilation(builder: KotlinCompilation.() -> Unit): KotlinCompilation {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }

    return KotlinCompilation().apply {
        compilerPluginRegistrars = listOf(ComposePluginRegistrar())
        useKsp2()
        symbolProcessorProviders.add(PreviewProcessor.Provider())

        // magic
        inheritClassPath = true
        messageOutputStream = System.out // see diagnostics in real time
        jvmTarget = "21"
        verbose = false

        builder()
    }
}

fun JvmCompilationResult.assertableGeneratedKspSources(
    compilation: KotlinCompilation,
): List<AssertableFile> {
    return sourcesGeneratedBySymbolProcessor.toList().map {
        it.descendantRelativeTo(compilation.kspSourcesDir).path hasContent
            org.assertj.core.util.Files.contentOf(it, Charsets.UTF_8).trim()
    }
}
