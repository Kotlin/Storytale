import org.gradle.kotlin.dsl.withType
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
    implementation(libs.kotlin.poet)
    implementation(libs.ksp.api)
    implementation(project(":modules:gradle-plugin"))
    testImplementation(compose.components.uiToolingPreview)
    testImplementation(compose.runtime)
    testImplementation(kotlin("compiler-embeddable"))
    testImplementation(kotlin("compose-compiler-plugin-embeddable"))
    testImplementation(kotlin("test"))
    testImplementation(libs.assertj.core)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinCompileTesting.ksp)
    testImplementation(project(":modules:runtime-api"))
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions.optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
}
