import java.util.Properties
import java.io.File
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
    implementation(libs.compose.gradle.plugin)
    implementation(libs.kotlin.poet)
}

group = "org.jetbrains.compose.storytale"

// Composite builds don't inherit properties from the root project.
// Read from the root's gradle.properties as a fallback.
val rootGradleProperties = Properties().apply {
    val rootProps = rootProject.rootDir.toPath().resolve("../../gradle.properties").toFile()
    if (rootProps.exists()) load(rootProps.inputStream())
}
version = findProperty("storytale.deploy.version")
    ?: rootGradleProperties.getProperty("storytale.deploy.version")
    ?: error("'storytale.deploy.version' was not set")

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
    repositories {
        maven {
            name = "ComposeRepo"
            setUrl(System.getenv("COMPOSE_REPO_URL"))
            credentials {
                username = System.getenv("COMPOSE_REPO_USERNAME")
                password = System.getenv("COMPOSE_REPO_KEY")
            }
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
