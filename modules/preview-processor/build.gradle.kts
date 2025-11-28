plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.kotlin.poet)
    implementation(libs.ksp.api)
    implementation(kotlin("compiler-embeddable"))
    implementation("org.jetbrains.compose.storytale:gradle-plugin")
}
