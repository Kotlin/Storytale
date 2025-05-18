package org.jetbrains.compose.storytale.plugin

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
        val multiplatformEnabled = project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")
        val androidApplicationEnabled = project.plugins.hasPlugin("com.android.application")
        val composeEnabled = project.plugins.hasPlugin("org.jetbrains.compose")
        val composePluginEnabled = project.plugins.hasPlugin("org.jetbrains.kotlin.plugin.compose")
        if (
            (composeEnabled || composePluginEnabled)
            && (multiplatformEnabled || androidApplicationEnabled)
        ) {
            val extension = project.extensions.create(
                STORYTALE_EXTENSION_NAME,
                StorytaleExtension::class.java,
                project,
            )
            project.processConfigurations(extension)
        }
    }

    override fun getCompilerPluginId() = COMPILER_PLUGIN_ID

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>) = true

    override fun getPluginArtifact() = SubpluginArtifact("org.jetbrains.compose.storytale", "compiler-plugin", VERSION)

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>) = kotlinCompilation.target.project.provider { emptyList<SubpluginOption>() }

    private fun Project.processConfigurations(extension: StorytaleExtension) {
        extension.targets.forEach {
            when (it) {
                is KotlinJsIrTarget ->
                    when (it.wasmTargetType) {
                        KotlinWasmTargetType.JS -> processWasmCompilation(extension, it)
                        null -> processJsCompilation(extension, it)
                        else -> {}
                    }
                is KotlinAndroidTarget -> processAndroidCompilation(extension, it)
                is KotlinJvmTarget -> processJvmCompilation(extension, it)
                is KotlinNativeTarget -> processNativeCompilation(extension, it)
            }
        }
    }

    companion object {
        const val COMPILER_PLUGIN_ID = "org.jetbrains.compose.storytale.compiler-plugin"
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

        val VERSION = BuildTimeConfig.projectVersion
    }
}
