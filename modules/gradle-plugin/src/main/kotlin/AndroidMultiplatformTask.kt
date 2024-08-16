package org.jetbrains.compose.plugin.storytale

import com.android.build.gradle.AppExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.task
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

fun Project.processAndroidCompilation(extension: StorytaleExtension, target: KotlinAndroidTarget) {
    project.logger.info("Configuring storytale for Kotlin on Android")
    val generatorTask = createAndroidStorytaleGenerateSourceTask(extension, target)
    createAndroidExecTask(target, extension, generatorTask)
}

val androidGradlePlugins = listOf(
    "com.android.application",
    "com.android.library",
)

fun createAndroidExecTask(
    target: KotlinAndroidTarget,
    extension: StorytaleExtension,
    generatorTask: AndroidSourceGeneratorTask
) {
    androidGradlePlugins.forEach {
        extension.project.plugins.withId(it) {
            val applicationExtension = extension.project.extensions.findByType(AppExtension::class)
//                ?: extension.project.extensions.findByType(LibraryExtension::class)
                ?: error("!!!")

            applicationExtension.buildTypes.create(StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX)
                .apply { initWith(applicationExtension.buildTypes.getByName("debug")) }

            applicationExtension.applicationVariants.configureEach {
                if (name != StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX) return@configureEach

                applicationExtension.sourceSets.getByName(StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX) {
                    this@getByName.kotlin.setSrcDirs(
                        extension.mainStoriesSourceSet.kotlin.srcDirs +
                        extension.androidStorySourceSet.kotlin.srcDirs +
                        listOf(generatorTask.outputSourcesDir)
                    )
                    manifest.srcFile(generatorTask.outputResourcesDir.resolve("AndroidManifest.xml"))
                }

                target.compilations.getByName(StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX).apply {
                    associateWith(target.compilations.getByName("debug"))
                }
            }
        }
    }
}

private fun Project.createAndroidStorytaleGenerateSourceTask(
    extension: StorytaleExtension,
    target: KotlinAndroidTarget
): AndroidSourceGeneratorTask {
    val storytaleBuildDir = extension.getBuildDirectory(target)
    return task<AndroidSourceGeneratorTask>("${target.name}${StorytaleGradlePlugin.STORYTALE_GENERATE_SUFFIX}") {
        group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
        description = "Generate Android source files for '${target.name}'"
        title = target.name
        outputResourcesDir = file("$storytaleBuildDir/resources")
        outputSourcesDir = file("$storytaleBuildDir/sources")
    }
}
