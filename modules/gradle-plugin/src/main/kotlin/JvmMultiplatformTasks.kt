package org.jetbrains.compose.plugin.storytale

import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.JavaExec
import org.gradle.jvm.toolchain.JavaLauncher
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.task
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

fun Project.processJvmCompilation(extension: StorytaleExtension, target: KotlinJvmTarget) {
  project.logger.info("Configuring storytale for Kotlin/JVM")
  createJvmStorytaleGenerateSourceTask(extension, target)

  val storytaleCompilation = createJvmStorytaleCompileTask(extension, target)

  val runtimeClasspath = storytaleCompilation.output.allOutputs + storytaleCompilation.runtimeDependencyFiles
  createJvmStorytaleExecTask(extension, storytaleCompilation, target, runtimeClasspath)
}

private fun Project.createJvmStorytaleGenerateSourceTask(
  extension: StorytaleExtension,
  target: KotlinJvmTarget
) {
  val storytaleBuildDir = extension.getBuildDirectory(target)
  task<JvmSourceGeneratorTask>("${target.name}${StorytaleGradlePlugin.STORYTALE_GENERATE_SUFFIX}") {
    group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
    description = "Generate JVM source files for '${target.name}'"
    title = target.name
    outputResourcesDir = file("$storytaleBuildDir/resources")
    outputSourcesDir = file("$storytaleBuildDir/sources")
  }
}

fun Project.createJvmStorytaleCompileTask(
  extension: StorytaleExtension,
  target: KotlinJvmTarget
): KotlinJvmCompilation {
  val mainCompilation = target.compilations.named(KotlinCompilation.MAIN_COMPILATION_NAME).get()
  val storytaleBuildDir = extension.getBuildDirectory(target)
  val storytaleCompilation =
    target.compilations.create(StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX) as KotlinJvmCompilation

  storytaleCompilation.associateWith(mainCompilation)

  storytaleCompilation.apply {
    val sourceSet = kotlinSourceSets.single()

    sourceSet.dependsOn(extension.mainStoriesSourceSet)

    sourceSet.kotlin.setSrcDirs(files("$storytaleBuildDir/sources"))

    val generateTaskName = "${target.name}${StorytaleGradlePlugin.STORYTALE_GENERATE_SUFFIX}"

    extension.project.afterEvaluate {
      sourceSet.resources.srcDirs("$storytaleBuildDir/resources", mainCompilation.defaultSourceSet.resources)

      extension.project.tasks.named(processResourcesTaskName).configure {
        dependsOn(generateTaskName)
      }
    }

    compileTaskProvider.configure {
      group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
      description = "Compile JVM storytale source files for '${target.name}'"

      dependsOn(generateTaskName)
    }
  }
  return storytaleCompilation
}

private fun Project.createJvmStorytaleExecTask(
  extension: StorytaleExtension,
  compilation: KotlinJvmCompilation,
  target: KotlinJvmTarget,
  runtimeClasspath: FileCollection
) {
  task<JavaExec>("${target.name}${StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX}Run") {
    dependsOn(compilation.compileTaskProvider)
    group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
    description = "Execute storytale for '${target.name}'"

    val storiesBuildDir = extension.getBuildDirectory(target)
    mainClass.set("org.jetbrains.compose.storytale.generated.MainKt")

    classpath(
      file("$storiesBuildDir/classes"),
      file("$storiesBuildDir/resources"),
      runtimeClasspath
    )

    javaLauncher.set(javaLauncherProvider())
  }
}

private fun Project.javaLauncherProvider(): Provider<JavaLauncher> = provider {
  val toolchainService = extensions.findByType(JavaToolchainService::class.java) ?: return@provider null
  val javaExtension = extensions.findByType(JavaPluginExtension::class.java) ?: return@provider null
  toolchainService.launcherFor(javaExtension.toolchain).orNull
}