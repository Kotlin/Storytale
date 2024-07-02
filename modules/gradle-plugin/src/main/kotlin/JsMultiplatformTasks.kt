package org.jetbrains.compose.plugin.storytale

import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.AbstractCopyTask
import org.gradle.kotlin.dsl.task
import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.web.tasks.UnpackSkikoWasmRuntimeTask
import org.jetbrains.kotlin.gradle.dsl.JsModuleKind
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrCompilation
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

fun Project.processJsCompilation(extension: StorytaleExtension, target: KotlinJsIrTarget) {
  project.logger.info("Configuring storytale for Kotlin/JS")
  createJsStorytaleGenerateSourceTask(extension, target)

  val storytaleCompilation = createWasmAndJsStorytaleCompilation(extension, target)

  createWasmAndJsStorytaleExecTask(storytaleCompilation)
}

fun Project.createWasmAndJsStorytaleCompilation(
  extension: StorytaleExtension,
  target: KotlinJsIrTarget
): KotlinJsIrCompilation {
  val mainCompilation = target.compilations.named(KotlinCompilation.MAIN_COMPILATION_NAME).get()
  val storytaleBuildDir = extension.getBuildDirectory(target)
  val storytaleCompilation =
    target.compilations.create(StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX) as KotlinJsIrCompilation

  storytaleCompilation.associateWith(mainCompilation)

  (mainCompilation.target as KotlinJsTargetDsl).apply {
    // force to create executable: required for IR, do nothing on Legacy
    binaries.executable(storytaleCompilation)
  }

  storytaleCompilation.apply {
    val sourceSet = kotlinSourceSets.single()

    sourceSet.dependsOn(extension.mainStoriesSourceSet)

    sourceSet.kotlin.setSrcDirs(files("$storytaleBuildDir/sources"))

    val generateTaskName = "${target.name}${StorytaleGradlePlugin.STORYTALE_GENERATE_SUFFIX}"

    extension.project.afterEvaluate {
      val unpackSkikoTask = extension.project.tasks.withType<UnpackSkikoWasmRuntimeTask>().single()

      sourceSet.resources.srcDirs(
        "$storytaleBuildDir/resources",
        unpackSkikoTask.outputDir,
        mainCompilation.defaultSourceSet.resources
      )

      extension.project.tasks.named(processResourcesTaskName).configure {
        dependsOn(unpackSkikoTask)
        dependsOn(generateTaskName)

        (this as? AbstractCopyTask)?.duplicatesStrategy = DuplicatesStrategy.INCLUDE
      }
    }

    compileTaskProvider.configure {
      group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
      description = "Compile web storytale source files for '${target.name}'"

      dependsOn(generateTaskName)
      configureOptions()
    }

    binaries.withType(JsIrBinary::class.java)
      .configureEach { linkTask.configure { configureOptions() } }
  }

  return storytaleCompilation
}

private fun Kotlin2JsCompile.configureOptions() {
  compilerOptions.apply {
    sourceMap.set(true)
    moduleKind.set(JsModuleKind.MODULE_ES)
  }
}

private fun Project.createJsStorytaleGenerateSourceTask(extension: StorytaleExtension, target: KotlinJsIrTarget) {
  val storytaleBuildDir = extension.getBuildDirectory(target)
  task<JsSourceGeneratorTask>("${target.name}${StorytaleGradlePlugin.STORYTALE_GENERATE_SUFFIX}") {
    group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
    description = "Generate JS source files for '${target.name}'"
    title = target.name
    outputResourcesDir = file("$storytaleBuildDir/resources")
    outputSourcesDir = file("$storytaleBuildDir/sources")
  }
}

fun createWasmAndJsStorytaleExecTask(compilation: KotlinJsIrCompilation) {
  val compilationTarget = compilation.target as KotlinJsIrTarget

  compilationTarget.browser.configureBuild(compilation)
  compilationTarget.browser.configureRun(compilation)
}