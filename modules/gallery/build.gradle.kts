import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.androidLibrary)
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
  androidTarget {
    publishLibraryVariants("release")

    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_11)
    }
  }

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
      }
    }

    val mobileMain by creating {
      dependsOn(commonMain)
    }

    val androidMain by getting {
      dependsOn(mobileMain)
    }

    val iosMain by getting {
      dependsOn(mobileMain)
    }

    val desktopMain by creating {
      dependsOn(commonMain)
    }

    val jsMain by getting {
      dependsOn(desktopMain)
    }

    val wasmJsMain by getting {
      dependsOn(desktopMain)
    }

    val jvmMain by getting {
      dependsOn(desktopMain)
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

publishing {}

android {
  namespace = "org.jetbrains.compose.storytale.gallery"
  compileSdk = libs.versions.android.compileSdk.get().toInt()
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  defaultConfig {
    minSdk = 24
  }
}
