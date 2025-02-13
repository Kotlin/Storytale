/*
 * Copyright 2014-2025 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
}

group = "org.jetbrains.compose.storytale"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.dokka.core)
    implementation(libs.dokka.base)
    implementation(libs.kotlinx.html)

    testImplementation(libs.jsoup)
    testImplementation(libs.dokka.test.api)
    testImplementation(libs.dokka.base.test.utils)
    testImplementation(libs.dokka.base)
    testImplementation(libs.junit.api)
    testImplementation(libs.kotlin.test)

}

tasks.withType<Test> {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

val emptyJavadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "storytale-dokka-plugin"
            from(components["kotlin"])
        }
        withType<MavenPublication> {
            artifact(emptyJavadocJar)
        }
    }
}