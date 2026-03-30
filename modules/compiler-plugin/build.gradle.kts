import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `maven-publish`
    kotlin("jvm")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        apiVersion = KotlinVersion.KOTLIN_2_1
        languageVersion = KotlinVersion.KOTLIN_2_1
        optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
    }
}

dependencies {
    compileOnly(compose.runtime)
    implementation(kotlin("compiler-embeddable"))
    testImplementation(compose.foundation)
    testImplementation(compose.material3)
    testImplementation(compose.runtime)
    testImplementation(compose.ui)
    testImplementation(kotlin("compose-compiler-plugin-embeddable"))
    testImplementation(kotlin("test"))
    testImplementation(libs.assertj.core)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinCompileTesting.core)
    testImplementation(project(":modules:runtime-api"))
}

sourceSets {
    val main by getting {
        kotlin.srcDirs("src/kotlin")
        resources.srcDir("src/resources")
    }
    val test by getting {
        kotlin.srcDirs("src/test/kotlin")
        resources.srcDir("src/test/resources")
    }
}

group = "org.jetbrains.compose.storytale"

val emptyJavadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "compiler-plugin"
            from(components["kotlin"])
        }
        withType<MavenPublication> {
            artifact(emptyJavadocJar)
        }
    }
}
