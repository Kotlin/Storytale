plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.kotlin.poet)
    implementation(libs.ksp.api)
    implementation(kotlin("compiler-embeddable"))
    implementation(project(":modules:gradle-plugin"))
}
