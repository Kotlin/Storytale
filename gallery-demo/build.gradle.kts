import org.jetbrains.compose.reload.ComposeHotRun
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
    id("org.jetbrains.compose.hot-reload") version "1.0.0-alpha03"
}

class StorytaleCompilerPlugin : KotlinCompilerPluginSupportPlugin {
    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        return kotlinCompilation.project.provider { emptyList() }
    }

    override fun getCompilerPluginId(): String {
        return "org.jetbrains.compose.compiler.plugins.storytale"
    }

    override fun getPluginArtifact(): SubpluginArtifact {
        return SubpluginArtifact("org.jetbrains.compose.storytale", "local-compiler-plugin")
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return kotlinCompilation.target.platformType in setOf(
            org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.jvm,
            org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.wasm,
        )
    }
}

apply<StorytaleCompilerPlugin>()

configurations.all {
    resolutionStrategy.dependencySubstitution {
        substitute(module("org.jetbrains.compose.storytale:local-compiler-plugin"))
            .using(project(":modules:compiler-plugin"))
    }
}

kotlin {
    js {
        browser()
        binaries.executable()
    }
    wasmJs {
        moduleName = "gallery-demo"
        browser {
            commonWebpackConfig {
                outputFileName = "gallery-demo.js"
            }
        }
        binaries.executable()
    }

    jvm("desktop")

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.navigation.compose)
                implementation(libs.compose.highlights)
                implementation(libs.kotlinx.serialization.json)
                implementation(projects.modules.runtimeApi)
                implementation(projects.modules.gallery)
                implementation("org.jetbrains.compose.material3.adaptive:adaptive:1.1.0-beta01")
            }
        }

        val desktopMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs = listOf(
            "-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-opt-in=kotlinx.coroutines.FlowPreview",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-opt-in=com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi",
            "-Xexpect-actual-classes",
        )
    }
}

compose.desktop {
    application {
        mainClass = "storytale.gallery.demo.MainKt"
        buildTypes.release.proguard {
            isEnabled.set(false)
        }
    }
}

composeCompiler {
    featureFlags.add(ComposeFeatureFlag.OptimizeNonSkippingGroups)
}

tasks.register<ComposeHotRun>("runHot") {
    mainClass.set("storytale.gallery.demo.MainKt")
}
