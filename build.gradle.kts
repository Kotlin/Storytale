plugins {
  // this is necessary to avoid the plugins to be loaded multiple times
  // in each subproject's classloader
  alias(libs.plugins.androidApplication) apply false
  alias(libs.plugins.androidLibrary) apply false
  alias(libs.plugins.jetbrainsCompose) apply false
  alias(libs.plugins.kotlinMultiplatform) apply false
  alias(libs.plugins.compose.compiler) apply false
}

subprojects {
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
}

inline fun <reified T> Project.configureIfExists(fn: T.() -> Unit) {
  extensions.findByType(T::class.java)?.fn()
}