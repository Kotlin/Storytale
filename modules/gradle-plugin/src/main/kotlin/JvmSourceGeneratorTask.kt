package org.jetbrains.compose.plugin.storytale

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
      addImport("org.jetbrains.compose.storytale.gallery", "Gallery")

      function("main") {
        addStatement("""
                |application {
                |   Window(onCloseRequest = ::exitApplication, title = "example") {
                |      Gallery()
                |   }
                |}""".trimMargin())
      }
    }.build()

    file.writeTo(outputSourcesDir)
  }
}