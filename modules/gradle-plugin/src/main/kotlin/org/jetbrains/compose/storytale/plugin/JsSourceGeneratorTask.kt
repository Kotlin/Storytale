package org.jetbrains.compose.storytale.plugin

import com.squareup.kotlinpoet.FileSpec
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.incremental.createDirectory

@CacheableTask
open class JsSourceGeneratorTask : DefaultTask() {

    @Input
    lateinit var title: String

    @OutputDirectory
    lateinit var outputResourcesDir: File

    @OutputDirectory
    lateinit var outputSourcesDir: File

    @TaskAction
    fun generate() {
        cleanup(outputSourcesDir)
        cleanup(outputResourcesDir)

        generateSources()
        generateResources()
    }

    private fun generateSources() {
        val file = FileSpec.builder(StorytaleGradlePlugin.STORYTALE_PACKAGE, "Main").apply {
            addImport("org.jetbrains.skiko.wasm", "onWasmReady")
            addMainFileImports()
            addMainViewControllerFun()
            function("main") {
                addStatement("onWasmReady { MainViewController() }")
            }
        }.build()

        file.writeTo(outputSourcesDir)
    }

    private fun generateResources() {
        if (!outputResourcesDir.exists()) {
            outputResourcesDir.createDirectory()
        }

        val stylesFile = File(outputResourcesDir, "styles.css")
        stylesFile.writeText(webStylesCssContent)

        val index = File(outputResourcesDir, "index.html")
        index.writeText(webIndexHtmlContent(SCRIPT_FILE_NAME))
    }

    companion object {
        internal const val SCRIPT_FILE_NAME = "composeApp.js"
    }
}
