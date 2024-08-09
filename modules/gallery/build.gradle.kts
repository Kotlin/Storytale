import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.serialization)
  `maven-publish`
}

kotlin {
  js()
  wasmJs()
  jvm()
  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    commonMain.dependencies {
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.ui)
      implementation(compose.components.resources)
      implementation(compose.components.uiToolingPreview)
      implementation(libs.navigation.compose)
      implementation(libs.kotlinx.serialization.json)
      implementation(projects.modules.runtimeApi)
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
      "-Xexpect-actual-classes"
    )
  }
}

group = "org.jetbrains.compose.storytale"
version = "1.0"

publishing {}
