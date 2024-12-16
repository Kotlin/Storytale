![Frame 482360](https://github.com/user-attachments/assets/b90b5776-f2f4-4385-8b7d-94eb912eacdf)

[![Incubator](https://jb.gg/badges/incubator-plastic.svg)](https://github.com/JetBrains#jetbrains-on-github)

# Storytale

Storytale is a Gradle Plugin designed to help developers to show their composables and develop them isolated by generating a gallery of the project components.
Check the `examples` and their generated web gallery [here](https://kotlin.github.io/Storytale)

Since Storytale is still in the early stages of development, the api is marked as unstable, but this section will also show you how to use `Storytale` to write code for your components, so let's get started! 🌟

<img width="1604" alt="All platforms" src="https://github.com/user-attachments/assets/b9a3d08f-7ff5-4a55-a0fc-904b4279e116">

## ⚙️ Getting Started

### 1. Setup

#### Import Dependencies

<details close>
  <summary>using <b>Version Catalog</b></summary>

> **libs.versions.toml**

```toml
[versions]
storytale = "0.0.1+dev3"

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

```kotlin
repositories {
  mavenCentral()
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}
```

> [!NOTE]  
> Storytale **has not** yet released its first version on `mavenCentral`. Currently, we publish dev builds to maven("https://maven.pkg.jetbrains.space/public/p/compose/dev"), so it's required to add this repository as shown above.

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
   val enabled by parameter(true)
   PrimaryButton(onClick = {}, enabled = enabled)
}
```

Next, let's run the `desktopStoriesRun` command, you can find it in the `project/Storytale` section on the right side of the Gradle panel.

<img width="485" alt="image" src="https://github.com/user-attachments/assets/a4ff8e1a-b549-4085-bac3-a4e6f8f246aa">

If you can't find all Gradle tasks containing `Storytale` after syncing, check if this option is enabled:

`settings->Experimental`

<img width="624" alt="image" src="https://github.com/user-attachments/assets/eed99739-121e-49ce-82ad-2217676a5869">


## Building and Contributing

Once the sync is successful, run `./gradlew publishToMavenLocal`.

At this point, if you see these Storytale `Gradle tasks`, it means you’ve successfully set up the project and can start contributing! :)

<img width="453" alt="image" src="https://github.com/user-attachments/assets/e9cfa634-27f2-4613-9579-194c3e6c09a4">

Before running `XXXXStoriesRun`, you need to run `./gradlew publishToMavenLocal` to deploy the latest changes if you've modified any part of the code (except for examples module)

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
