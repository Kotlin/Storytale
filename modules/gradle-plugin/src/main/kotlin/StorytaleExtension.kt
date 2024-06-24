package org.jetbrains.compose.plugin.storytale

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer

open class StorytaleExtension(internal val project: Project) {
  var buildDir: String = "storytale"

  private val multiplatformExtension = run {
    val multiplatformClass =
      tryGetClass<KotlinMultiplatformExtension>(
        className = "org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension"
      )
    multiplatformClass?.let { project.extensions.findByType(it) } ?: error("UNEXPECTED")
  }

  val targets = multiplatformExtension.targets

  val mainStoriesSourceSet by lazy {
    project
      .extensions
      .getByType(KotlinSourceSetContainer::class)
      .sourceSets
      .create("common${StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX}")
      .apply {
        dependencies {
          implementation("org.jetbrains.compose.storytale:gallery:1.0")
          implementation("org.jetbrains.compose.storytale:runtime:1.0")
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