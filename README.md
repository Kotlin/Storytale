[![Incubator](https://jb.gg/badges/incubator-plastic.svg)](https://github.com/JetBrains#jetbrains-on-github)

# Storytale

## Build and run

To build and run storytale example:

1. In IntelliJ IDEA, open the repository.
2. In terminal run the following command:
    `./gradlew publishToMavenLocal`
3. Uncomment all the matchings for `alias(libs.plugins.storytale)`
4. Run the application using a storytale run task:
    ```sh
    # to run desktop stories
   ./gradlew :examples:desktopStoriesRun
    # to run wasm stories
   ./gradlew :examples:wasmJsBrowserStoriesDevelopmentRun
   ```