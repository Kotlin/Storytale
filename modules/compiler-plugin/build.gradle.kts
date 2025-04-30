import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    `kotlin-dsl`
    `maven-publish`
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("compiler-embeddable"))
    testImplementation("junit:junit:4.13.2")
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
