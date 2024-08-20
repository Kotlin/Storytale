package org.jetbrains.compose.plugin.storytale

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.KotlinWasmTargetType
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

class StorytaleGradlePlugin : KotlinCompilerPluginSupportPlugin {

  override fun apply(project: Project) {
    project.plugins.withId("org.jetbrains.kotlin.multiplatform") {
      project.plugins.withId("org.jetbrains.compose") {
        val extension =
          project.extensions.create(STORYTALE_EXTENSION_NAME, StorytaleExtension::class.java, project)
        project.processConfigurations(extension)
      }
    }
  }

  override fun getCompilerPluginId() = PLUGIN_ID

  override fun isApplicable(kotlinCompilation: KotlinCompilation<*>) = true

  override fun getPluginArtifact() =
    SubpluginArtifact(
      "org.jetbrains.compose",
      "storytale-compiler-plugin",
      "1.0"
    )

  override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>) =
    kotlinCompilation.target.project.provider { emptyList<SubpluginOption>() }

  private fun Project.processConfigurations(extension: StorytaleExtension) {
    extension.multiplatformExtension.applyDefaultHierarchyTemplate()
    extension.targets.configureEach {
      when (this) {
        is KotlinJsIrTarget ->
          when (wasmTargetType) {
            KotlinWasmTargetType.JS -> processWasmCompilation(extension, this)
            null -> processJsCompilation(extension, this)
            else -> {}
          }
        is KotlinAndroidTarget -> processAndroidCompilation(extension, this)
        is KotlinJvmTarget -> processJvmCompilation(extension, this)
        is KotlinNativeTarget -> processNativeCompilation(extension, this)
      }
    }
  }

  companion object {
    const val PLUGIN_ID = "org.jetbrains.compose.storytale"
    const val STORYTALE_TASK_GROUP = "storytale"
    const val STORYTALE_EXTENSION_NAME = "storytale"
    const val STORYTALE_PACKAGE = "org.jetbrains.compose.storytale.generated"
    const val STORYTALE_GENERATE_SUFFIX = "StorytaleGenerate"
    const val STORYTALE_SOURCESET_SUFFIX = "Stories"
    const val STORYTALE_EXEC_SUFFIX = "Stories"
    const val STORYTALE_EXEC_PREFIX = "stories"
    const val STORYTALE_NATIVE_APP_NAME = "StorytaleGalleryApp"
    const val STORYTALE_NATIVE_PROJECT_NAME = "StorytaleXCode"
    const val STORYTALE_NATIVE_PROJECT_PATH = "Compose.StorytaleXCode"
    const val STORYTALE_DEVICE_NAME = "Storytale iOS Device"
    const val DERIVED_DATA_DIRECTORY_NAME = "dd"
    const val LINK_BUILD_VERSION = "Debug"
  }
}