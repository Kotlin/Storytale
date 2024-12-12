import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
  `kotlin-dsl`
  `maven-publish`
  kotlin("jvm")
}

dependencies {
  implementation(kotlin("compiler-embeddable"))
}

sourceSets {
  val main by getting {
    java.srcDirs("src")
    resources.srcDir("src/resources")
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
