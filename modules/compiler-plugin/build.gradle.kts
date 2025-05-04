import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    `kotlin-dsl`
    `maven-publish`
    kotlin("jvm")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}
dependencies {
    compileOnly(compose.runtime)

    implementation(kotlin("compiler-embeddable"))
    testImplementation("dev.zacsweers.kctfork:core:0.7.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.27.2")
    testImplementation(project(":modules:runtime-api"))

    testImplementation(compose.runtime)
    testImplementation(compose.foundation)
    testImplementation(compose.material3)
    testImplementation(compose.ui)
    testImplementation(kotlin("compose-compiler-plugin-embeddable"))
    // testImplementation("com.jetbrains.intellij.platform:util:233.13135.128")
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

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions.optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
}
