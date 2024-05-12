import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
  androidTarget()
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs()
  jvm("desktop")
}

android {
  namespace = "org.jetbrains.storytale.runtime"
  compileSdk = 34
}