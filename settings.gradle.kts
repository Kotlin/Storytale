rootProject.name = "Storytale"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  repositories {
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    google {
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
  }
}

dependencyResolutionManagement {
  repositories {
    google {
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    mavenCentral()
    mavenLocal()
  }
}

plugins {
  id("com.gradle.develocity") version "3.17.5"
}

develocity {
  buildScan {
    termsOfUseUrl = "https://gradle.com/terms-of-service"
    termsOfUseAgree = "yes"
    val isOffline = providers.provider { gradle.startParameter.isOffline }.getOrElse(false)
    val ci = System.getenv("GITHUB_ACTIONS") == "true"
    publishing {
      onlyIf { !isOffline && (it.buildResult.failures.isNotEmpty() || ci) }
    }
  }
}


include(":modules:gallery")
include(":modules:gradle-plugin")
include(":modules:compiler-plugin")
include(":modules:runtime")
include(":examples")
