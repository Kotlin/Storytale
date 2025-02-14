package org.jetbrains.compose.storytale.plugin

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

open class StorytaleExtension(internal val project: Project) {
    var buildDir: String = "storytale"

    val multiplatformExtension = run {
        val multiplatformClass =
            tryGetClass<KotlinMultiplatformExtension>(
                className = "org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension",
            )
        multiplatformClass?.let { project.extensions.findByType(it) } ?: error("UNEXPECTED")
    }

    val targets = multiplatformExtension.targets

    val mainStoriesSourceSet by lazy {
        multiplatformExtension
            .sourceSets
            .create("common${StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX}")
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
