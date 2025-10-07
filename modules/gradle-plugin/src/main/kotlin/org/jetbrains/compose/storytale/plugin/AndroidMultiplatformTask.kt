package org.jetbrains.compose.storytale.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import java.io.ByteArrayOutputStream
import java.io.File
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.task
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

val androidGradlePlugins = listOf("com.android.application")

fun Project.processAndroidCompilation(extension: StorytaleExtension, target: KotlinAndroidTarget) {
    project.logger.info("Configuring storytale for Kotlin on Android")
    createAndroidCompilationTasks(target, extension)
}

fun Project.createAndroidCompilationTasks(
    target: KotlinAndroidTarget,
    extension: StorytaleExtension,
) {
    androidGradlePlugins.forEach { pluginId ->
        extension.project.plugins.withId(pluginId) {
            val storytaleBuildDir = extension.getBuildDirectory(target)
            val storytaleBuildSourcesDir = file("$storytaleBuildDir/sources")
            val storytaleBuildResourcesDir = file("$storytaleBuildDir/resources")
            val applicationExtension = extension.project.extensions.findByType(AppExtension::class)
                ?: error("Android Application plugin must be applied to the module")

            val mainStoriesSourceSet = extension.mainStoriesSourceSet

            applicationExtension.buildTypes.create(StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX)
                .apply {
                    initWith(applicationExtension.buildTypes.getByName("debug"))
                    applicationIdSuffix = ".${StorytaleGradlePlugin.STORYTALE_EXEC_PREFIX}"
                }

            applicationExtension.sourceSets
                .matching { it.name == StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX }
                .configureEach {
                    manifest.srcFile(storytaleBuildResourcesDir.resolve("AndroidManifest.xml"))
                }

            project.kotlinExtension.sourceSets
                .matching { it.name == "android${StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX}" }
                .configureEach {
                    kotlin.srcDir(storytaleBuildSourcesDir)
                    extension.setupCommonStoriesSourceSetDependencies(this)
                    mainStoriesSourceSet.kotlin.srcDirs.forEach {
                        kotlin.srcDir(it)
                    }
                }

            applicationExtension.applicationVariants
                .matching { it.name == StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX }
                .configureEach {
                    val generatorTask = createAndroidStorytaleGenerateSourceTask(
                        target,
                        this,
                        storytaleBuildSourcesDir,
                        storytaleBuildResourcesDir,
                    )

                    extension.project.tasks
                        .matching { it.name == "process${StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX}MainManifest" }
                        .configureEach { dependsOn(generatorTask) }

                    target.compilations
                        .matching { it.name == StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX }
                        .configureEach {
                            associateWith(target.compilations.getByName("debug"))
                            compileTaskProvider.configure {
                                dependsOn(generatorTask)
                                dependsOn(":generateResourceAccessorsForCommonStories")
                            }
                        }

                    val startEmulatorTask = createStartEmulatorTask(target, applicationExtension)

                    task("${target.name}${StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX}Run") {
                        group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP

                        val adbPath = applicationExtension.adbExecutable.absolutePath
                        val activityPath = "$applicationId.StorytaleAppActivity"

                        dependsOn(startEmulatorTask)
                        dependsOn("install${this@configureEach.name}")

                        doLast {
                            exec {
                                commandLine(adbPath, "shell", "am", "start", "-n", "$applicationId/$activityPath")
                            }
                        }
                    }
                }
        }
    }
}

private fun Project.createAndroidStorytaleGenerateSourceTask(
    target: KotlinAndroidTarget,
    applicationVariant: ApplicationVariant,
    buildSourcesDir: File,
    buildResourcesDir: File,
) = task<AndroidSourceGeneratorTask>("${target.name}${StorytaleGradlePlugin.STORYTALE_GENERATE_SUFFIX}") {
    group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
    description = "Generate Android source files for '${target.name}'"
    title = target.name
    appPackageName = applicationVariant.applicationId
    outputSourcesDir = buildSourcesDir
    outputResourcesDir = buildResourcesDir
}

private fun Project.createStartEmulatorTask(target: KotlinAndroidTarget, applicationExtension: AppExtension): Task {
    return task("${target.name}${StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX}StartEmulator") {
        doLast {
            val adbPath = applicationExtension.adbExecutable.absolutePath

            // First check whether any physical devices are connected
            val connectedDevices = ByteArrayOutputStream().use { output ->
                exec {
                    commandLine(adbPath, "devices")
                    standardOutput = output
                }
                output.toString().trim().lines()
                    .drop(1) // skip "List of devices attached"
                    .filter { it.isNotBlank() }
                    .map { it.split("\t")[0] } // get device ID
            }

            // find the physical device
            val physicalDevice = connectedDevices.firstOrNull { !it.startsWith("emulator-") }

            if (physicalDevice != null) {
                project.logger.info("Using physical device: $physicalDevice")
                exec {
                    commandLine(adbPath, "-s", physicalDevice, "wait-for-device")
                }
            } else {
                val output = ByteArrayOutputStream()
                val emulatorPath = applicationExtension.sdkDirectory.resolve("emulator/emulator")
                exec {
                    commandLine(emulatorPath, "-list-avds")
                    standardOutput = output
                }

                val emulatorName = output.toString().trim().lineSequence().lastOrNull()

                if (emulatorName != null) {
                    project.logger.info("Starting emulator: $emulatorName")
                    Thread {
                        exec {
                            commandLine(emulatorPath, "-avd", emulatorName, "-no-snapshot-load")
                        }
                    }.start()

                    exec {
                        commandLine(
                            adbPath,
                            "wait-for-device",
                            "shell",
                            "while [[ -z $(getprop sys.boot_completed) ]]; do sleep 1; done;",
                        )
                    }
                } else {
                    throw GradleException("No available Android emulators.")
                }
            }
        }
    }
}
