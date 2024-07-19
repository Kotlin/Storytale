![Frame 482360](https://github.com/user-attachments/assets/53bdefc7-45a5-47be-9055-d079a811b7d8)

[![Incubator](https://jb.gg/badges/incubator-plastic.svg)](https://github.com/JetBrains#jetbrains-on-github)

# Storytale

Storytale is a Compose Multiplatform library designed to help developers more easily test their written @Composable components. It offers convenient and concise APIs, along with a well-designed editor to simplify the testing process.

Since Storytale is still in the early stages of development, the api is marked as unstable, but this section will also show you how to use `Storytale` to write code for your components, so let's get started! 🌟

## ⚙️ Getting Started

### 1. Setup

#### Import Dependencies

<details open>
  <summary>using <b>Version Catalog</b></summary>

> **libs.versions.toml**

```toml
[versions]
storytale = "1.0"

[plugins]
storytale = { id = "org.jetbrains.compose.storytale", version.ref = "storytale" }
```

> **build.gradle.kts** `root level`
```kotlin
plugins {
  alias(libs.plugins.storytale) apply false
}
```
</details>

> **build.gradle.kts** `app level`
```kotlin
plugins {
  alias(libs.plugins.storytale)
}
```
</details>

Storytale is published on **Maven Central**, so if you **haven't** defined the repository yet, please do it:

```kotlin
repositories {
  mavenCentral()
}
```

### 2. Create Sourcesets for Storytale on the target platform (for multi-platform projects)

Storytale can be used for Compose Multiplatform projects. To start using the Storytale API, you need to define a sourceset for the component you want to test (for example, it might only be used for `Android/iOS` platforms, or it could be common for all platforms).

In your app's 'src' folder, go to New -> Directory:

<img width="355" alt="image" src="https://github.com/user-attachments/assets/a9dc68a9-3a28-4d26-a128-f0372b52d08b">

### 3. Usage

Now, your project structure will look like this:

```
└── src/
    ├── androidMain
    ├── commonMain
    ├── xxxxxStories/
    │   └── kotlin
    └── desktopMain
```

Let's try to write a simple function in `commonMain`:

`commonMain/PrimaryButton.kt`
```kotlin
@Composable
fun PrimaryButton(onClick: () -> Unit, enabled: Boolean = true) {
  Button(onClick = onClick, enabled = enabled) {
    Text("Click me!")
  }
}
```

`commonStories/kotlin/PrimaryButton.story.kt`
```kotlin
import org.jetbrains.compose.storytale.story

val `PrimaryButton default state` by story {
   PrimaryButton(onClick = {})
}
```

Next, let's run the `desktopStoriesRun` command, you can find it in the `project/Storytale` section on the right side of the Gradle panel.

<img width="485" alt="image" src="https://github.com/user-attachments/assets/a4ff8e1a-b549-4085-bac3-a4e6f8f246aa">

If you can't find all Gradle tasks containing `Storytale` after syncing, check if this option is enabled:

`settings->Experimental`

<img width="624" alt="image" src="https://github.com/user-attachments/assets/eed99739-121e-49ce-82ad-2217676a5869">


## Building and Contributing

For your first build of Storytale, you need to comment out `alias(libs.plugins.storytale)` in the `build.gradle.kts` file under the `examples module` (if it’s not already commented). Then, sync Gradle.

Once the sync is successful, uncomment it and sync again, then run `./gradlew publishToMavenLocal`.

At this point, if you see these Storytale `Gradle tasks`, it means you’ve successfully set up the project and can start contributing! :)

<img width="453" alt="image" src="https://github.com/user-attachments/assets/e9cfa634-27f2-4613-9579-194c3e6c09a4">

#### About project structure

```
.
└── modules/
    ├── compiler-plugin
    ├── gallery
    ├── gradle-plugin
    └── runtime
```

##### compiler-plugin

Includes the entry point of the Storytale compiler plugin and its related features.

##### gallery

The gallery represents the final, fully functional multi-platform application that is produced by Storytale.

##### gradle-plugin

All aspects related to building Storytale, including various Gradle tasks, generating Storytale apps for different platforms, and so on.

#### runtime

The runtime module is designed to provide developers with essential APIs during the coding process
