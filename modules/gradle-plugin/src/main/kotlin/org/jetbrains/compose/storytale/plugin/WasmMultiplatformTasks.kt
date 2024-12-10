package org.jetbrains.compose.storytale.plugin

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget

fun Project.processWasmCompilation(extension: StorytaleExtension, target: KotlinJsIrTarget) {
  project.logger.info("Configuring storytale using Kotlin/Wasm")
  createWasmStorytaleGenerateSourceTask(extension, target)

  val storytaleCompilation = createWasmAndJsStorytaleCompilation(extension, target)

  createWasmAndJsStorytaleExecTask(storytaleCompilation)
}

private fun Project.createWasmStorytaleGenerateSourceTask(extension: StorytaleExtension, target: KotlinJsIrTarget) {
  val storytaleBuildDir = extension.getBuildDirectory(target)
  tasks.register("${target.name}${StorytaleGradlePlugin.STORYTALE_GENERATE_SUFFIX}", WasmSourceGeneratorTask::class.java) {
    group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
    description = "Generate Wasm source files for '${target.name}'"
    title = target.name
    outputSourcesDir = file("$storytaleBuildDir/sources")
    outputResourcesDir = file("$storytaleBuildDir/resources")
  }
}