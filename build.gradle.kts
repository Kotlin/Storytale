import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.ksp) apply false
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = libs.versions.kotlin.asProvider().get()))
    }
}

subprojects {
    version = findProperty("storytale.deploy.version")
        ?: error("'storytale.deploy.version' was not set")

    plugins.withId("maven-publish") {
        configureIfExists<PublishingExtension> {
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
    }
    plugins.apply(rootProject.libs.plugins.spotless.get().pluginId)
    extensions.configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            targetExclude("src/test/resources/**")
            ktlint(libs.ktlint.get().version)
                .editorConfigOverride(
                    mapOf(
                        "indent_size" to "4",
                        "ktlint_compose_modifier-missing-check" to "disabled",
                        "ktlint_compose_compositionlocal-allowlist" to "disabled",
                    ),
                )
                .customRuleSets(listOf(libs.composeRules.get().toString()))
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint(libs.ktlint.get().version)
                .editorConfigOverride(
                    mapOf("indent_size" to "4"),
                )
        }
    }
}

// includeBuild() provides automatic plugin substitution but root tasks
// don't propagate to composite builds. Wire in the gradle-plugin publish.
// We use afterEvaluate to ensure all plugins are applied before we reference tasks.
gradle.projectsEvaluated {
    tasks.register("publishToMavenLocal") {
        group = "publishing"
        description = "Publish all modules (including gradle-plugin) to Maven Local"

        // Depend on subprojects that have maven-publish applied
        subprojects.forEach { subproject ->
            if (subproject.plugins.hasPlugin("maven-publish")) {
                dependsOn(subproject.tasks.named("publishToMavenLocal"))
            }
        }
        // Also publish the gradle-plugin composite build
        dependsOn(gradle.includedBuild("gradle-plugin").task(":publishToMavenLocal"))
    }

    tasks.register("publishAllPublicationsToComposeRepoRepository") {
        group = "publishing"
        description = "Publish all modules (including gradle-plugin) to ComposeRepo"

        // Depend on subprojects that have the ComposeRepo task
        subprojects.forEach { subproject ->
            if (subproject.tasks.findByName("publishAllPublicationsToComposeRepoRepository") != null) {
                dependsOn(subproject.tasks.named("publishAllPublicationsToComposeRepoRepository"))
            }
        }
        // Also publish the gradle-plugin composite build
        dependsOn(gradle.includedBuild("gradle-plugin").task(":publishAllPublicationsToComposeRepoRepository"))
    }
}

inline fun <reified T> Project.configureIfExists(fn: T.() -> Unit) {
    extensions.findByType(T::class.java)?.fn()
}
