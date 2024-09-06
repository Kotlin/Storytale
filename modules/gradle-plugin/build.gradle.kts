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
      implementationClass = "org.jetbrains.compose.plugin.storytale.StorytaleGradlePlugin"
    }
  }
}

dependencies {
  implementation(libs.kotlin.gradle.plugin)
  implementation(libs.android.gradle.plugin)
  implementation(libs.kotlin.compiler.embeddable)
  implementation(libs.compose.gradle.plugin)
  implementation(libs.kotlin.poet)
}

group = "org.jetbrains.compose"
version = libs.versions.storytale.get()

val emptyJavadocJar by tasks.registering(Jar::class) {
  archiveClassifier.set("javadoc")
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      artifactId = "storytale"
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
    packageName.set("org.jetbrains.compose.plugin.storytale")
    objectName.set("BuildTimeConfig")
    destination.set(project.layout.buildDirectory.get().asFile)

    configProperties {
      val PROJECT_VERSION: String by string(libs.versions.storytale.get())
    }
  }
}