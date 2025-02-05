@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package org.jetbrains.compose.storytale.plugin

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Property
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.task
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.resources.resolve.ResolveResourcesFromDependenciesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.konan.target.Architecture
import org.jetbrains.kotlin.konan.target.KonanTarget

fun Project.processNativeCompilation(extension: StorytaleExtension, target: KotlinNativeTarget) {
  if (target.konanTarget !in setOf(KonanTarget.IOS_ARM64, KonanTarget.IOS_SIMULATOR_ARM64, KonanTarget.IOS_X64)) {
    return
  }
  project.logger.info("Configuring storytale for Kotlin/Native")
  val generatorTask = createNativeStorytaleGenerateSourceTask(extension, target)
  val compilation = createNativeStorytaleCompileTask(extension, target, generatorTask)
  createNativeStorytaleExecTask(compilation, extension, target)
}

private fun Project.createNativeStorytaleCompileTask(
  extension: StorytaleExtension,
  target: KotlinNativeTarget,
  generatorTask: NativeSourceGeneratorTask,
): KotlinNativeCompilation {
  val storytaleBuildDir = extension.getBuildDirectory(target)
  val mainCompilation = target.compilations.named(KotlinCompilation.MAIN_COMPILATION_NAME).get()
  val storytaleCompilation =
    target.compilations.create(StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX) as KotlinNativeCompilation

  storytaleCompilation.associateWith(mainCompilation)
  setupResourceResolvingForTarget(storytaleBuildDir, storytaleCompilation)

  storytaleCompilation.target.apply {
    binaries.framework(StorytaleGradlePlugin.STORYTALE_TASK_GROUP) {
      baseName = StorytaleGradlePlugin.STORYTALE_NATIVE_APP_NAME
      isStatic = true
      compilation = storytaleCompilation
    }
  }

  storytaleCompilation.apply {
    defaultSourceSet.dependsOn(extension.mainStoriesSourceSet)
    defaultSourceSet.kotlin.setSrcDirs(files("$storytaleBuildDir/sources"))

    val resolveDependencyResourcesTask = extension.project.tasks
      .getByName(storytaleCompilation.resolveDependencyResourcesTaskName) as ResolveResourcesFromDependenciesTask

    defaultSourceSet.resources.srcDirs(
      "$storytaleBuildDir/resources",
      resolveDependencyResourcesTask.outputDirectory,
      mainCompilation.defaultSourceSet.resources,
    )

    compileTaskProvider.configure {
      dependsOn(generatorTask)
      dependsOn(resolveDependencyResourcesTask)
    }
  }

  return storytaleCompilation
}

private fun Project.createNativeStorytaleGenerateSourceTask(
  extension: StorytaleExtension,
  target: KotlinNativeTarget,
): NativeSourceGeneratorTask {
  val storytaleBuildDir = extension.getBuildDirectory(target)
  return task<NativeSourceGeneratorTask>("${target.name}${StorytaleGradlePlugin.STORYTALE_GENERATE_SUFFIX}") {
    group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
    description = "Generate Native source files for '${target.name}'"
    title = target.name
    outputResourcesDir = file("$storytaleBuildDir/resources")
    outputSourcesDir = file("$storytaleBuildDir/sources")
  }
}

private fun Project.createNativeStorytaleExecTask(
  compilation: KotlinNativeCompilation,
  extension: StorytaleExtension,
  target: KotlinNativeTarget,
): Task? {
  if (!target.name.contains("Simulator")) return null

  val deviceId = objects.property<String>()
  val targetSuffix = target.name.capitalized()
  val linkTask = tasks.findByPath("link${StorytaleGradlePlugin.STORYTALE_TASK_GROUP.capitalized()}${StorytaleGradlePlugin.LINK_BUILD_VERSION}Framework$targetSuffix") as? KotlinNativeLink
    ?: error("Link task was not created for target ${target.name}")

  val unzipXCodeProjectTask = createUnzipResourceTask(extension)
  val simulatorRegistrationTask = createSimulatorRegistrationTask(unzipXCodeProjectTask, targetSuffix, deviceId)

  val buildTask = createBuildTask(targetSuffix, deviceId, unzipXCodeProjectTask, simulatorRegistrationTask, linkTask)
  val platform = if (target.konanTarget === KonanTarget.IOS_SIMULATOR_ARM64) "iphonesimulator" else "iphoneos"

  val copyResourcesTask = createCopyNativeResourcesTask(platform, target, targetSuffix, compilation, unzipXCodeProjectTask, buildTask)
  val installAppTask = createInstallApplicationToSimulatorTask(deviceId, targetSuffix, platform, unzipXCodeProjectTask, buildTask, copyResourcesTask)

  return task("${target.name}${StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX}Run") {
    group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
    dependsOn(unzipXCodeProjectTask)
    dependsOn(installAppTask)

    inputs.property("deviceId", deviceId)

    doLast {
      exec {
        workingDir = unzipXCodeProjectTask.outputDir.get().asFile
        commandLine(
          "/usr/bin/xcrun",
          "simctl",
          "launch",
          deviceId.get(),
          StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_PATH,
        )
      }
      exec {
        commandLine("/usr/bin/open", "-a", "Simulator")
      }
    }
  }
}

private fun Project.createUnzipResourceTask(extension: StorytaleExtension): UnzipResourceTask {
  return task<UnzipResourceTask>("${StorytaleGradlePlugin.STORYTALE_TASK_GROUP}UnzipXCodeProject") {
    resourcePath.set("${StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_NAME}.zip")
    outputDir.set(
      file(
        layout.buildDirectory.get().asFile
          .resolve(extension.buildDir)
          .resolve(StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_NAME),
      ),
    )
  }
}

private fun Project.createSimulatorRegistrationTask(unzipResourceTask: UnzipResourceTask, targetSuffix: String, deviceIdProperty: Property<String>): Task {
  return task("${StorytaleGradlePlugin.STORYTALE_TASK_GROUP}Register$targetSuffix") {
    group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
    dependsOn(unzipResourceTask)

    doLast {
      val deviceId: String

      val simulatorName = StorytaleGradlePlugin.STORYTALE_DEVICE_NAME
      val deviceList = execute("/usr/bin/xcrun", "simctl", "list", "devices")
      val matches = Regex("""$simulatorName \(([0-9A-F-]+)\) \((\w+)\)""").find(deviceList)

      val existingDeviceId = matches?.groups?.get(1)?.value
      val isBooted = matches?.groups?.get(2)?.value == "Booted"

      if (existingDeviceId == null) {
        val availableSimulator = findAvailableIPhoneSimulator()
        val availableRuntime = findAvailableIOsRuntime()

        deviceId = execute("/usr/bin/xcrun", "simctl", "create", simulatorName, availableSimulator, availableRuntime).trim()
      } else {
        deviceId = existingDeviceId
      }

      if (!isBooted) exec { commandLine("/usr/bin/xcrun", "simctl", "boot", deviceId) }

      deviceIdProperty.set(deviceId)
    }
  }
}

private fun Project.findAvailableIPhoneSimulator(): String {
  val availableDeviceTypes = execute("/usr/bin/xcrun", "simctl", "list", "devicetypes")
  val deviceTypeMatches = Regex("""(com.apple.CoreSimulator.SimDeviceType.iPhone-[A-Za-z0-9\-]+)""").findAll(availableDeviceTypes).toList()

  return deviceTypeMatches.lastOrNull()?.groups?.get(1)?.value ?: throw GradleException(
    """
      No iPhone device found. Please download any iPhone simulator.
      Refer to the official documentation: https://developer.apple.com/documentation/xcode/installing-additional-simulator-runtimes
    """.trimIndent(),
  )
}

private fun Project.findAvailableIOsRuntime(): String {
  val availableRuntimes = execute("/usr/bin/xcrun", "simctl", "list", "runtimes")
  val runtimeMatches = Regex("""(com.apple.CoreSimulator.SimRuntime.iOS-[0-9\-]+)""").findAll(availableRuntimes).toList()

  return runtimeMatches.lastOrNull()?.groups?.get(1)?.value ?: throw GradleException(
    """
      No iOS runtime found. Please download any iOS runtimes.
      Refer to the official documentation: https://developer.apple.com/documentation/xcode/installing-additional-simulator-runtimes
    """.trimIndent(),
  )
}

private fun Project.createBuildTask(
  targetSuffix: String,
  deviceId: Property<String>,
  unzipResourceTask: UnzipResourceTask,
  simulatorRegistrationTask: Task,
  linkTask: KotlinNativeLink,
): Task {
  return task("${StorytaleGradlePlugin.STORYTALE_TASK_GROUP}Build$targetSuffix") {
    group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
    dependsOn(unzipResourceTask)
    dependsOn(simulatorRegistrationTask)
    dependsOn(linkTask)

    inputs.property("deviceId", deviceId)

    val xcodeProjectPath = unzipResourceTask.outputDir.get().asFile
    inputs.files(linkTask.outputs.files)
    outputs.dir(xcodeProjectPath.resolve(StorytaleGradlePlugin.DERIVED_DATA_DIRECTORY_NAME))

    doLast {
      val frameworkPath = linkTask.destinationDirectory.asFile.get().path
      exec {
        workingDir = xcodeProjectPath
        commandLine(
          "/usr/bin/xcodebuild",
          "clean",
          "build",
          "-project",
          "${StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_NAME}/${StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_NAME}.xcodeproj",
          "-scheme",
          StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_NAME,
          "-destination",
          "id=${deviceId.get()}",
          "-derivedDataPath",
          StorytaleGradlePlugin.DERIVED_DATA_DIRECTORY_NAME,
          "FRAMEWORK_SEARCH_PATHS=$frameworkPath",
        )
      }
    }
  }
}

private fun Project.createCopyNativeResourcesTask(
  platform: String,
  target: KotlinNativeTarget,
  targetSuffix: String,
  compilation: KotlinNativeCompilation,
  unzipResourceTask: UnzipResourceTask,
  buildTask: Task,
): NativeCopyResourcesTask {
  val frameworkResources = files().apply {
    compilation.allKotlinSourceSets.forAll { from(it.resources.sourceDirectories) }
  }

  return task<NativeCopyResourcesTask>("${StorytaleGradlePlugin.STORYTALE_TASK_GROUP}CopyResources$targetSuffix") {
    dependsOn(frameworkResources)
    dependsOn(unzipResourceTask)
    dependsOn(buildTask)

    xcodeTargetPlatform.set(platform)
    xcodeTargetArchs.set(if (target.konanTarget.architecture === Architecture.X64) "x86_64" else "arm64")
    resourceFiles.set(frameworkResources)

    val appPath = unzipResourceTask.outputDir.get().asFile
      .resolve(StorytaleGradlePlugin.DERIVED_DATA_DIRECTORY_NAME)
      .resolve("Build/Products")
      .resolve("${StorytaleGradlePlugin.LINK_BUILD_VERSION}-$platform")
      .resolve("${StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_NAME}.app")

    outputDir.set(appPath.resolve("compose-resources"))
  }
}

private fun Project.createInstallApplicationToSimulatorTask(
  deviceId: Property<String>,
  targetSuffix: String,
  platform: String,
  unzipResourceTask: UnzipResourceTask,
  buildTask: Task,
  copyResourcesTask: Task,
): Task {
  return task("${StorytaleGradlePlugin.STORYTALE_TASK_GROUP}InstallApp$targetSuffix") {
    group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
    dependsOn(unzipResourceTask)
    dependsOn(buildTask)
    dependsOn(copyResourcesTask)

    inputs.property("deviceId", deviceId)

    doLast {
      exec {
        workingDir = unzipResourceTask.outputDir.get().asFile
        val appPath = "${StorytaleGradlePlugin.DERIVED_DATA_DIRECTORY_NAME}/Build/Products/${StorytaleGradlePlugin.LINK_BUILD_VERSION}-$platform/${StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_NAME}.app"
        commandLine(
          "/usr/bin/xcrun",
          "simctl",
          "install",
          deviceId.get(),
          appPath,
        )
      }
    }
  }
}
