package org.jetbrains.compose.plugin.storytale

import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.gradle.AppExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.HasKotlinDependencies
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.ide.dependencyResolvers.IdeCInteropMetadataDependencyClasspathResolver.dependencies
import org.jetbrains.kotlin.gradle.plugin.mpp.resources.resourcesPublicationExtension

open class StorytaleExtension(internal val project: Project) {
  var buildDir: String = "storytale"

  val multiplatformExtension = run {
    val multiplatformClass =
      tryGetClass<KotlinMultiplatformExtension>(
        className = "org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension"
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
        implementation("org.jetbrains.compose.storytale:gallery:1.0")
        implementation("org.jetbrains.compose.storytale:runtime-api:1.0")
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