package org.jetbrains.compose.plugin.storytale

import org.gradle.api.Project
import org.gradle.kotlin.dsl.task
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget

fun Project.processWasmCompilation(extension: StorytaleExtension, target: KotlinJsIrTarget) {
  project.logger.info("Configuring storytale using Kotlin/Wasm")
  createWasmStorytaleGenerateSourceTask(extension, target)

  val storytaleCompilation = createWasmAndJsStorytaleCompilation(extension, target)

  createWasmAndJsStorytaleExecTask(storytaleCompilation)
}

private fun Project.createWasmStorytaleGenerateSourceTask(extension: StorytaleExtension, target: KotlinJsIrTarget) {
  val storytaleBuildDir = extension.getBuildDirectory(target)
  task<WasmSourceGeneratorTask>("${target.name}${StorytaleGradlePlugin.STORYTALE_GENERATE_SUFFIX}") {
    group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
    description = "Generate Wasm source files for '${target.name}'"
    title = target.name
    outputSourcesDir = file("$storytaleBuildDir/sources")
    outputResourcesDir = file("$storytaleBuildDir/resources")
  }
}