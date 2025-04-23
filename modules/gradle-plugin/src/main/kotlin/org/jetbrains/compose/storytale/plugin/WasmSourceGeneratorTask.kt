package org.jetbrains.compose.storytale.plugin

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.incremental.createDirectory

@CacheableTask
open class WasmSourceGeneratorTask : DefaultTask() {
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
            addMainFileImports()
            addMainViewControllerFun()
            function("main") {
                addStatement("MainViewController()")
            }
        }.build()

        file.writeTo(outputSourcesDir)
    }

    private fun generateResources() {
        if (!outputResourcesDir.exists()) {
            outputResourcesDir.createDirectory()
        }

        val styles = File(outputResourcesDir, "styles.css")
        styles.writeText(webStylesCssContent)


        val index = File(outputResourcesDir, "index.html")
        index.writeText(webIndexHtmlContent(JsSourceGeneratorTask.SCRIPT_FILE_NAME))
    }
}

internal fun FileSpec.Builder.addMainFileImports() {
    addImport("androidx.compose.ui.window", "ComposeViewport")
    addImport("kotlinx.browser", "document")
    addImport("kotlinx.browser", "window")
    addImport("org.jetbrains.compose.storytale.gallery", "Gallery")
    addImport("org.jetbrains.compose.storytale.gallery.story.code", "JetBrainsMonoRegularRes")
    addImport("org.jetbrains.compose.resources", "preloadFont")
}

internal fun FileSpec.Builder.addMainViewControllerFun() {
    val optInExperimentalComposeUi = AnnotationSpec.builder(ClassName("kotlin", "OptIn")).addMember(
        "androidx.compose.ui.ExperimentalComposeUiApi::class, org.jetbrains.compose.resources.ExperimentalResourceApi::class"
    ).build()

    function("MainViewController") {
        addAnnotation(optInExperimentalComposeUi)
        addStatement(
            """
                |val useEmbedded = window.location.search.contains("embedded=true")
                |
                |ComposeViewport(document.body!!) {
                |   val hasResourcePreloadCompleted = preloadFont(JetBrainsMonoRegularRes).value != null
                |
                |   if (hasResourcePreloadCompleted) {
                |       Gallery(isEmbedded = useEmbedded)
                |   }
                |
                |}
                |
                    """.trimMargin(),
        )
    }
}

internal val webStylesCssContent = """
            |html, body {
            |    width: 100%;
            |    height: 100%;
            |    margin: 0;
            |    padding: 0;
            |    overflow: hidden;
            |}
            """.trimMargin()

internal fun webIndexHtmlContent(jsFileName: String): String {
    return """
            |<!DOCTYPE html>
            |<html lang="en">
            |  <head>
            |    <meta charset="UTF-8">
            |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
            |    <title>Gallery</title>
            |    <link type="text/css" rel="stylesheet" href="styles.css">
            |    <script type="application/javascript" src="skiko.js"></script>
            |  </head>
            |  <body>
            |      <script type="application/javascript" src="${jsFileName}"></script>
            |  </body>
            |</html>
            """.trimMargin()
}
