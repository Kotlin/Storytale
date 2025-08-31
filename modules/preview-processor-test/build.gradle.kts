import org.gradle.kotlin.dsl.compileOnly
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm()
    androidTarget()

    sourceSets {
        val commonMain by getting {
            dependencies {
                compileOnly(compose.runtime)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.components.uiToolingPreview)
                implementation(project(":modules:preview-processor"))
                implementation(kotlin("test"))

                implementation(compose.runtime)
                implementation(kotlin("compiler-embeddable"))
                implementation(kotlin("compose-compiler-plugin-embeddable"))
                implementation(kotlin("test"))
                implementation(libs.assertj.core)
                implementation(libs.junit)
                implementation(libs.kotlinCompileTesting.ksp)
                implementation(project(":modules:runtime-api"))
            }
        }
        val androidUnitTest by getting {
            dependsOn(jvmMain)

            dependencies {
                implementation("androidx.compose.ui:ui-tooling-preview-android:1.7.0")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("androidx.compose.ui:ui-tooling-preview-desktop:1.7.0")
            }
        }
    }
}

android {
    compileSdk = 35
    namespace = "org.jetbrains.compose.storytale.preview.processor.test"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions.optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
}
