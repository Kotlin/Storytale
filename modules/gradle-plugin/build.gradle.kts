plugins {
  `kotlin-dsl`
  `java-gradle-plugin`
  `maven-publish`
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
  implementation(libs.kotlin.compiler.embeddable)
  implementation(libs.compose.gradle.plugin)
  implementation(libs.kotlin.poet)
}

group = "org.jetbrains.compose"
version = "1.0"

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
