package org.jetbrains.compose.plugin.storytale

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
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
    extension.targets.all {
      when (this) {
        is KotlinJsIrTarget ->
          when (wasmTargetType) {
            KotlinWasmTargetType.JS -> processWasmCompilation(extension, this)
            null -> processJsCompilation(extension, this)
            else -> {}
          }
        is KotlinJvmTarget -> processJvmCompilation(extension, this)
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
  }
}