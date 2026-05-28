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
version = findProperty("storytale.deploy.version")
    ?: "unspecified"

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
