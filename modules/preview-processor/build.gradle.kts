import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.ComposePlugin.CommonComponentsDependencies
import org.jetbrains.compose.ComposePlugin.CommonComponentsDependencies.uiToolingPreview
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    compileOnly(compose.runtime)
    implementation(libs.ksp.api)
    testImplementation(compose.components.uiToolingPreview)
    testImplementation(compose.runtime)
    testImplementation(kotlin("test"))
    testImplementation(libs.assertj.core)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinCompileTesting.ksp)
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions.optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
}
