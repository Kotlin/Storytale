package org.jetbrains.compose.storytale.plugin

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidExtension

class AndroidStorytaleExtension(project: Project) : StorytaleExtension(project) {
    private val kotlinAndroidExtension = run {
        val androidClass =
            tryGetClass<KotlinAndroidExtension>(
                className = "org.jetbrains.kotlin.gradle.dsl.KotlinAndroidExtension",
            )
        androidClass?.let { project.extensions.findByType(it) } ?: error("UNEXPECTED")
    }

    override val targets = listOf(kotlinAndroidExtension.target)

    override val mainStoriesSourceSet by lazy {
        kotlinAndroidExtension
            .sourceSets
            .create("main${StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX}")
            .apply { setupCommonStoriesSourceSetDependencies(this) }
    }
}
