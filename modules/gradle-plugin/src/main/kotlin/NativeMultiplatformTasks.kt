package org.jetbrains.compose.plugin.storytale

import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.task
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.resources.resolve.AggregateResourcesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.konan.target.Architecture
import org.jetbrains.kotlin.konan.target.KonanTarget
import java.io.ByteArrayOutputStream

fun Project.processNativeCompilation(extension: StorytaleExtension, target: KotlinNativeTarget) {
    if (target.konanTarget !in setOf(KonanTarget.IOS_ARM64, KonanTarget.IOS_SIMULATOR_ARM64, KonanTarget.IOS_X64)) return
    project.logger.info("Configuring storytale for Kotlin/Native")
    val generatorTask = createNativeStorytaleGenerateSourceTask(extension, target)
    val compilation = createNativeStorytaleCompileTask(extension, target, generatorTask)
    createNativeStorytaleExecTask(compilation, extension, target)
}

private fun Project.createNativeStorytaleCompileTask(
    extension: StorytaleExtension,
    target: KotlinNativeTarget,
    generatorTask: NativeSourceGeneratorTask
): KotlinNativeCompilation  {
    val storytaleBuildDir = extension.getBuildDirectory(target)
    val mainCompilation = target.compilations.named(KotlinCompilation.MAIN_COMPILATION_NAME).get()
    val storytaleCompilation =
        target.compilations.create(StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX) as KotlinNativeCompilation

    storytaleCompilation.associateWith(mainCompilation)
    extension.resourcesPublicationExtension.setupResourceResolvingForTarget(target, storytaleCompilation)

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

        val aggregateResourcesTask = extension.project.tasks
            .getByName("${storytaleCompilation.name.lowercase()}${target.name.capitalized()}AggregateResources") as AggregateResourcesTask

        defaultSourceSet.resources.srcDirs(
            "$storytaleBuildDir/resources",
            aggregateResourcesTask.outputDirectory,
            mainCompilation.defaultSourceSet.resources,
        )

        compileTaskProvider.configure {
            dependsOn(generatorTask)
            dependsOn(aggregateResourcesTask)
        }
    }

    return storytaleCompilation
}

private fun Project.createNativeStorytaleGenerateSourceTask(
    extension: StorytaleExtension,
    target: KotlinNativeTarget
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
    target: KotlinNativeTarget
) {
    if (!target.name.contains("Simulator")) return

    var deviceId: String? = null
    val targetSuffix = target.name.capitalized()
    val linkTask = tasks.findByPath("link${StorytaleGradlePlugin.STORYTALE_TASK_GROUP.capitalized()}${StorytaleGradlePlugin.LINK_BUILD_VERSION}Framework$targetSuffix") as? KotlinNativeLink
        ?: error("Link task was not created for target ${target.name}")

    val unzipXCodeProjectTask = task<UnzipResourceTask>("${StorytaleGradlePlugin.STORYTALE_TASK_GROUP}UnzipXCodeProject") {
        resourcePath.set("${StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_NAME}.zip")
        outputDir.set(file(layout.buildDirectory.get().asFile.resolve(extension.buildDir).resolve(StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_NAME)))
    }

    val simulatorRegistrationTask = task("${StorytaleGradlePlugin.STORYTALE_TASK_GROUP}Register$targetSuffix") {
        group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
        dependsOn(unzipXCodeProjectTask)

        val output = ByteArrayOutputStream()
        val simulatorName = StorytaleGradlePlugin.STORYTALE_DEVICE_NAME
        exec {
            commandLine("/usr/bin/xcrun", "simctl", "list", "devices")
            standardOutput = output
        }
        val deviceList = output.toString()
        val matches = Regex("""$simulatorName \(([0-9A-F-]+)\) \((\w+)\)""").find(deviceList)
        val existingDeviceId = matches?.groups?.get(1)?.value
        val isBooted = matches?.groups?.get(2)?.value == "Booted"

        if (existingDeviceId == null) {
            val createOutput = ByteArrayOutputStream()
            exec {
                commandLine("/usr/bin/xcrun", "simctl", "create", simulatorName, "com.apple.CoreSimulator.SimDeviceType.iPhone-12-Pro-Max", "com.apple.CoreSimulator.SimRuntime.iOS-17-4")
                standardOutput = createOutput
            }
            deviceId = createOutput.toString().trim()
        } else {
            deviceId = existingDeviceId
        }
        if (!isBooted) {
            exec {
                commandLine("/usr/bin/xcrun", "simctl", "boot", deviceId!!)
            }
        }
    }

    val frameworkResources = files()
    compilation.allKotlinSourceSets.forAll { ss ->
        frameworkResources.from(ss.resources.sourceDirectories)
    }

    val buildTask = task("${StorytaleGradlePlugin.STORYTALE_TASK_GROUP}Build$targetSuffix") {
        group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
        dependsOn(unzipXCodeProjectTask)
        dependsOn(simulatorRegistrationTask)
        dependsOn(linkTask)

        val xcodeProjectPath = unzipXCodeProjectTask.outputDir.get().asFile
        outputs.dir(xcodeProjectPath.resolve(StorytaleGradlePlugin.DERIVED_DATA_DIRECTORY_NAME))

        doLast {
            val frameworkPath = linkTask.destinationDirectory.asFile.get().path
            exec {
                workingDir = xcodeProjectPath
                commandLine("/usr/bin/xcodebuild", "clean", "build", "-project", "${StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_NAME}/${StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_NAME}.xcodeproj", "-scheme", StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_NAME, "-destination", "id=${deviceId!!}", "-derivedDataPath", StorytaleGradlePlugin.DERIVED_DATA_DIRECTORY_NAME, "FRAMEWORK_SEARCH_PATHS=$frameworkPath")
            }
        }
    }

    val platform = if (target.konanTarget === KonanTarget.IOS_SIMULATOR_ARM64) "iphonesimulator" else "iphoneos"

    val copyResourcesTask = task<NativeCopyResourcesTask>("${StorytaleGradlePlugin.STORYTALE_TASK_GROUP}CopyResources$targetSuffix") {
        dependsOn(frameworkResources)
        dependsOn(unzipXCodeProjectTask)
        dependsOn(buildTask)

        xcodeTargetPlatform.set(platform)
        xcodeTargetArchs.set(if (target.konanTarget.architecture === Architecture.X64) "x86_64" else "arm64")
        resourceFiles.set(frameworkResources)

        val appPath = unzipXCodeProjectTask.outputDir.get().asFile
            .resolve(StorytaleGradlePlugin.DERIVED_DATA_DIRECTORY_NAME)
            .resolve("Build/Products")
            .resolve("${StorytaleGradlePlugin.LINK_BUILD_VERSION}-$platform")
            .resolve("${StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_NAME}.app")

        outputDir.set(appPath.resolve("compose-resources"))
    }

    val installAppTask = task("${StorytaleGradlePlugin.STORYTALE_TASK_GROUP}InstallApp$targetSuffix") {
        group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
        dependsOn(unzipXCodeProjectTask)
        dependsOn(buildTask)
        dependsOn(copyResourcesTask)

        doLast {
            exec {
                workingDir = unzipXCodeProjectTask.outputDir.get().asFile
                val appPath = "${StorytaleGradlePlugin.DERIVED_DATA_DIRECTORY_NAME}/Build/Products/${StorytaleGradlePlugin.LINK_BUILD_VERSION}-$platform/${StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_NAME}.app"
                commandLine("/usr/bin/xcrun", "simctl", "install", deviceId!!, appPath)
            }
        }
    }

    task("${target.name}${StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX}Run") {
        group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
        dependsOn(unzipXCodeProjectTask)
        dependsOn(installAppTask)
        doLast {
            exec {
                workingDir = unzipXCodeProjectTask.outputDir.get().asFile
                commandLine("/usr/bin/xcrun", "simctl", "launch", deviceId!!, StorytaleGradlePlugin.STORYTALE_NATIVE_PROJECT_PATH)
            }
            exec {
                commandLine("/usr/bin/open", "-a", "Simulator")
            }
        }
    }
}
