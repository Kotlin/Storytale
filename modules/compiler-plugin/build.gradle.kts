import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
  `kotlin-dsl`
  `maven-publish`
  kotlin("jvm")
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.0")
}

sourceSets {
  val main by getting {
    java.srcDirs("src")
    resources.srcDir("src/resources")
  }
}

group = "org.jetbrains.compose"
version = libs.versions.storytale.get()

val emptyJavadocJar by tasks.registering(Jar::class) {
  archiveClassifier.set("javadoc")
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      artifactId = "storytale-compiler-plugin"
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
