rootProject.name = "Storytale"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

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
        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies/")
                }
            }
            filter {
                includeGroupByRegex("org\\.jetbrains\\.intellij\\.deps.*")
            }
        }
        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://www.jetbrains.com/intellij-repository/releases/")
                }
            }
            filter {
                includeGroup("com.jetbrains.intellij.platform")
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

include(":examples")
include(":gallery-demo")
include(":modules:gallery")
includeBuild("modules/gradle-plugin")
include(":modules:compiler-plugin")
include(":modules:dokka-plugin")
include(":modules:runtime-api")
include(":modules:preview-processor")
include(":modules:preview-processor-test")
