plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.compose.compiler)
  `maven-publish`
}

kotlin {
  wasmJs()
  js()
  jvm()
  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    commonMain.dependencies {
      implementation(compose.runtime)
    }
  }
}

group = "org.jetbrains.compose.storytale"
version = "1.0"

publishing {}