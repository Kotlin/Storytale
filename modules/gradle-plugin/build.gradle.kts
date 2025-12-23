import java.util.Properties
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    alias(libs.plugins.buildTimeConfig)
}

gradlePlugin {
    plugins {
        create("storytale") {
            id = "org.jetbrains.compose.storytale"
            implementationClass = "org.jetbrains.compose.storytale.plugin.StorytaleGradlePlugin"
        }
    }
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.compiler.embeddable)
    implementation(libs.compose.gradle.plugin)
    implementation(libs.compose.hot.reload.plugin)
    implementation(libs.kotlin.poet)
}

val rootProperties = Properties().apply {
    file("../../gradle.properties").inputStream().use { load(it) }
}

val storytaleVersion: String = rootProperties.getProperty("storytale.deploy.version")
    ?: error("storytale.deploy.version not found in root gradle.properties")

group = "org.jetbrains.compose.storytale"
version = storytaleVersion

val emptyJavadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "gradle-plugin"
            from(components["kotlin"])
        }
        withType<MavenPublication> {
            artifact(emptyJavadocJar)
        }
    }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    friendPaths.setFrom(libraries)
}

buildTimeConfig {
    config {
        packageName.set("org.jetbrains.compose.storytale.plugin")
        objectName.set("BuildTimeConfig")
        destination.set(project.layout.buildDirectory.get().asFile)

        configProperties {
            val projectVersion: String by string(version as String)
        }
    }
}
