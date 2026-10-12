package org.jetbrains.compose.storytale.plugin

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

open class StorytaleExtension(internal val project: Project) {
    var buildDir: String = "storytale"

    val multiplatformProject: Boolean
    val targets: List<KotlinTarget>
    val mainStoriesSourceSet: KotlinSourceSet

    init {
        val multiplatformExtension = tryGetClass<KotlinMultiplatformExtension>(
            className = "org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension",
        )
            ?.let { project.extensions.findByType(it) }

        val kotlinAndroidExtension = tryGetClass<KotlinAndroidExtension>(
            className = "org.jetbrains.kotlin.gradle.dsl.KotlinAndroidExtension",
        )
            ?.let { project.extensions.findByType(it) }

        val extension = checkNotNull(multiplatformExtension ?: kotlinAndroidExtension) {
            "UNEXPECTED"
        }
        val multiplatformTargets = multiplatformExtension
            ?.targets
            ?.toList()
        val androidTarget = kotlinAndroidExtension
            ?.target
            ?.let { listOf(it) }

        multiplatformProject = multiplatformExtension != null
        targets = multiplatformTargets ?: androidTarget ?: error("UNEXPECTED")
        mainStoriesSourceSet = extension.sourceSets
            .create(
                if (multiplatformExtension != null) {
                    "common${StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX}"
                } else {
                    StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX.lowercase()
                },
            )
            .apply { setupCommonStoriesSourceSetDependencies(this) }
    }

    internal fun setupCommonStoriesSourceSetDependencies(sourceSet: KotlinSourceSet) {
        with(sourceSet) {
            dependencies {
                implementation("org.jetbrains.compose.storytale:gallery:${StorytaleGradlePlugin.VERSION}")
                implementation("org.jetbrains.compose.storytale:runtime-api:${StorytaleGradlePlugin.VERSION}")
            }
        }
    }

    private fun <T> Any.tryGetClass(className: String): Class<T>? {
        val classLoader = javaClass.classLoader
        return try {
            @Suppress("UNCHECKED_CAST")
            Class.forName(className, false, classLoader) as Class<T>
        } catch (e: ClassNotFoundException) {
            null
        }
    }
}
