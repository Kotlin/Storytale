package org.jetbrains.compose.storytale.plugin

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
open class NativeSourceGeneratorTask : DefaultTask() {
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
      addImport("androidx.compose.ui.window", "ComposeUIViewController")
      addImport("org.jetbrains.compose.storytale.gallery", "Gallery")

      val uIViewControllerType = ClassName("platform.UIKit", "UIViewController")
      function("MainViewController") {
        returns(uIViewControllerType)
        addStatement("""
                |return ComposeUIViewController {
                |  Gallery()
                |}""".trimMargin())
      }
    }.build()

    file.writeTo(outputSourcesDir)
  }
}