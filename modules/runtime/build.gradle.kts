import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
//  kotlin("jvm")
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.jetbrainsCompose)
}

kotlin {
  androidTarget()
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs()

  js { browser() }

  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget = "11"
      }
    }
  }

  jvm("desktop")

  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64()
  ).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "ComposeApp"
      isStatic = true
    }
  }

  sourceSets {
    commonMain.dependencies {
      implementation(compose.runtime)
    }
  }
}

android {
  namespace = "org.jetbrains.storytale.runtime"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}
