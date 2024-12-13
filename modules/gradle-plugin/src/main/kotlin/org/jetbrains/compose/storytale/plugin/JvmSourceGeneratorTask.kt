package org.jetbrains.compose.storytale.plugin

import com.squareup.kotlinpoet.FileSpec
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
open class JvmSourceGeneratorTask : DefaultTask() {
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
  }

  private fun generateSources() {
    val file = FileSpec.builder(StorytaleGradlePlugin.STORYTALE_PACKAGE, "Main").apply {
      addImport("androidx.compose.ui.window", "Window")
      addImport("androidx.compose.ui.window", "application")
      addImport("androidx.compose.ui.unit", "DpSize")
      addImport("androidx.compose.ui.unit", "dp")
      addImport("androidx.compose.ui.window", "rememberWindowState")
      addImport("org.jetbrains.compose.storytale.gallery", "Gallery")

      function("MainViewController") {
        addStatement("""
          |application {
          |   Window(
          |      onCloseRequest = ::exitApplication,
          |      title = "example",
          |      state = rememberWindowState(size = DpSize(1440.dp, 920.dp))
          |   ) {
          |      Gallery()
          |   }
          |}""".trimMargin()
        )
      }

      function("main") {
        addStatement("MainViewController()")
      }
    }.build()

    file.writeTo(outputSourcesDir)
  }
}