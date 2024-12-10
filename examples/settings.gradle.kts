rootProject.name = "StorytaleUsageExamples"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

val storytaleVersion =
  providers.gradleProperty("storytale.deploy.version").orNull?.takeIf(String::isNotBlank)

pluginManagement {
  repositories {
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
    mavenCentral()
    mavenLocal()
  }

  if (storytaleVersion != null) {
    versionCatalogs.register("libs") {
      version("storytale", storytaleVersion)
    }
  }
}