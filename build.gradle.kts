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
            "ktlint_compose_modifier-missing-check" to "disabled",
            "ktlint_compose_compositionlocal-allowlist" to "disabled",
          ),
        )
        .customRuleSets(listOf(libs.composeRules.get().toString()))
    }
    kotlinGradle {
      ktlint(libs.ktlint.get().version)
    }
  }
}

inline fun <reified T> Project.configureIfExists(fn: T.() -> Unit) {
  extensions.findByType(T::class.java)?.fn()
}

