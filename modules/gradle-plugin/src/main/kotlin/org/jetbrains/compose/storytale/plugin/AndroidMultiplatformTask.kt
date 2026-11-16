package org.jetbrains.compose.storytale.plugin

import java.io.File
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.task
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

val androidGradlePlugins = listOf(
    "com.android.application",
    "com.android.library",
    "com.android.kotlin.multiplatform.library",
)

fun Project.processAndroidCompilation(extension: StorytaleExtension, target: KotlinTarget) {
    project.logger.info("Configuring storytale for Kotlin on Android")
    createAndroidCompilationTasks(target, extension)
}

fun Project.createAndroidCompilationTasks(
    target: KotlinTarget,
    extension: StorytaleExtension,
) {
    var configured = false
    val configureAction = {
        if (!configured) {
            val isApplication = extension.project.plugins.hasPlugin("com.android.application")
            val isLibrary = extension.project.plugins.hasPlugin("com.android.library") ||
                extension.project.plugins.hasPlugin("com.android.kotlin.multiplatform.library")

            if (isApplication) {
                configured = true
                configureAndroidApplication(target, extension)
            } else if (isLibrary) {
                configured = true
                configureAndroidLibrary(target, extension)
            }
        }
    }

    androidGradlePlugins.forEach { pluginId ->
        extension.project.plugins.withId(pluginId) {
            configureAction()
        }
    }
}

private fun Project.configureAndroidApplication(
    target: KotlinTarget,
    extension: StorytaleExtension,
) {
    val storytaleBuildDir = extension.getBuildDirectory(target)
    val storytaleBuildSourcesDir = file("$storytaleBuildDir/sources")
    val storytaleBuildResourcesDir = file("$storytaleBuildDir/resources")
    val mainStoriesSourceSet = extension.mainStoriesSourceSet

    AndroidComponentsHelper.configureApplicationMinSdk(project, StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX)

    val applicationExt = project.extensions.findByType(ApplicationExtension::class.java)
    if (applicationExt != null) {
        val debugBuildType = applicationExt.buildTypes.findByName("debug")
        applicationExt.buildTypes.maybeCreate(StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX).apply {
            if (debugBuildType != null) {
                initWith(debugBuildType)
            }
            applicationIdSuffix = ".${StorytaleGradlePlugin.STORYTALE_EXEC_PREFIX}"
            val debugSigning = applicationExt.signingConfigs.findByName("debug")
            if (debugSigning != null) {
                signingConfig = debugSigning
            }
            matchingFallbacks += listOf("debug", "release")
            isDebuggable = true
        }
        applicationExt.sourceSets.findByName(StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX)?.manifest?.srcFile(
            storytaleBuildResourcesDir.resolve("AndroidManifest.xml")
        )
    } else {
        configureLegacyApplicationDsl(storytaleBuildResourcesDir)
    }

    project.kotlinExtension.sourceSets
        .matching { it.name == "android${StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX}" }
        .configureEach {
            kotlin.srcDir(storytaleBuildSourcesDir)
            extension.setupCommonStoriesSourceSetDependencies(this)
            mainStoriesSourceSet.kotlin.srcDirs.forEach {
                kotlin.srcDir(it)
            }
        }

    AndroidComponentsHelper.onVariant(project, StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX) { variantName, appIdProvider ->
        val generatorTask = createAndroidStorytaleGenerateSourceTask(
            target,
            appIdProvider,
            storytaleBuildSourcesDir,
            storytaleBuildResourcesDir,
        )

        tasks
            .matching {
                it.name.contains("Manifest", ignoreCase = true) &&
                    (it.name.contains(variantName, ignoreCase = true) || it.name.contains("Storytale", ignoreCase = true))
            }
            .configureEach { dependsOn(generatorTask) }

        tasks
            .matching { it.name.endsWith("AarMetadata", ignoreCase = true) }
            .configureEach { enabled = false }

        if (target is KotlinAndroidTarget) {
            target.compilations
                .matching { it.name == StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX }
                .configureEach {
                    val debugCompilation = target.compilations.findByName("debug")
                    if (debugCompilation != null) {
                        associateWith(debugCompilation)
                    }
                    compileTaskProvider.configure {
                        dependsOn(generatorTask)
                        dependsOn(tasks.matching { it.name == "generateResourceAccessorsForCommonStories" })
                    }
                }
        }

        val adbPath = AndroidComponentsHelper.resolveAdbPath(project)
        val startEmulatorTask = createStartEmulatorTask(target, adbPath)

        val installTaskName = "install${variantName.capitalized()}"
        val packageTaskName = "package${variantName.capitalized()}"

        task("${target.name}${StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX}Run") {
            group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
            description = "Run Storytale gallery on Android"
            notCompatibleWithConfigurationCache("Launches interactive Android application on device or emulator")

            dependsOn(startEmulatorTask)
            val installTask = tasks.findByName(installTaskName)
            if (installTask != null) {
                dependsOn(installTask)
            } else {
                dependsOn(packageTaskName)
            }

            doLast {
                val appId = appIdProvider.get()
                val activityPath = "$appId.StorytaleAppActivity"
                val activeDevice = AndroidComponentsHelper.resolveActiveDeviceSerial(adbPath)
                val adbCmd = if (!activeDevice.isNullOrBlank()) {
                    listOf(adbPath, "-s", activeDevice)
                } else {
                    listOf(adbPath)
                }

                if (installTask == null) {
                    val apkDir = layout.buildDirectory.asFile.get().resolve("outputs/apk/$variantName")
                    val apkFile = apkDir.walkTopDown().firstOrNull { it.isFile && it.extension == "apk" }
                    if (apkFile != null) {
                        logger.lifecycle("Storytale: Installing APK via $adbPath on ${activeDevice ?: "default"}...")
                        runProcess(*(adbCmd + listOf("install", "-r", apkFile.absolutePath)).toTypedArray())
                    }
                }

                logger.lifecycle("Storytale: Launching activity $appId/$activityPath via $adbPath (device: ${activeDevice ?: "default"})")
                runProcess(*(adbCmd + listOf("shell", "am", "start", "-n", "$appId/$activityPath")).toTypedArray())
            }
        }
    }
}

private fun Project.configureAndroidLibrary(
    target: KotlinTarget,
    extension: StorytaleExtension,
) {
    project.logger.info("Configured Storytale for Android Library module '${project.path}'.")
    val storytaleBuildDir = extension.getBuildDirectory(target)
    val storytaleBuildSourcesDir = file("$storytaleBuildDir/sources")
    val storytaleBuildResourcesDir = file("$storytaleBuildDir/resources")
    val mainStoriesSourceSet = extension.mainStoriesSourceSet

    // Dynamically enable device testing on target if available
    try {
        val androidLib = (project.extensions.findByName("kotlin") as? org.gradle.api.plugins.ExtensionAware)
            ?.extensions?.findByName("androidLibrary")
            ?: (project.extensions.findByName("kotlin") as? org.gradle.api.plugins.ExtensionAware)
                ?.extensions?.findByName("android")
        val withDeviceTestMethod = target.javaClass.methods.firstOrNull {
            it.name == "withDeviceTest" && it.parameterCount == 1
        } ?: project.extensions.findByName("android")?.javaClass?.methods?.firstOrNull {
            it.name == "withDeviceTest" && it.parameterCount == 1
        } ?: androidLib?.javaClass?.methods?.firstOrNull {
            it.name == "withDeviceTest" && it.parameterCount == 1
        }
        if (withDeviceTestMethod != null) {
            val receiver = if (androidLib != null && withDeviceTestMethod.declaringClass.isAssignableFrom(androidLib.javaClass)) {
                androidLib
            } else if (withDeviceTestMethod.declaringClass.isAssignableFrom(target.javaClass)) {
                target
            } else {
                project.extensions.findByName("android")
            }
            if (withDeviceTestMethod.parameterTypes[0] == Function1::class.java) {
                val fn: (Any) -> Unit = {}
                withDeviceTestMethod.invoke(receiver, fn)
            } else {
                val action = Action<Any> { }
                withDeviceTestMethod.invoke(receiver, action)
            }
        }
    } catch (e: Throwable) {
        project.logger.debug("Storytale: could not invoke withDeviceTest on target", e)
    }

    project.kotlinExtension.sourceSets
        .matching {
            it.name == "androidDeviceTest" ||
                it.name == "androidTest" ||
                it.name == "android${StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX}"
        }
        .configureEach {
            kotlin.srcDir(storytaleBuildSourcesDir)
            extension.setupCommonStoriesSourceSetDependencies(this)
            mainStoriesSourceSet.kotlin.srcDirs.forEach {
                kotlin.srcDir(it)
            }
            dependencies {
                implementation("androidx.activity:activity-compose:1.10.1")
            }
        }

    target.compilations.configureEach {
        compileTaskProvider.configure {
            dependsOn(tasks.matching { it.name == "generateResourceAccessorsForCommonStories" })
        }
    }

    val deviceTestSourceSetName = project.kotlinExtension.sourceSets
        .firstOrNull { it.name == "androidDeviceTest" || it.name == "androidTest" }?.name ?: "androidDeviceTest"
    val deviceTestManifest = project.file("src/$deviceTestSourceSetName/AndroidManifest.xml")

    tasks.matching { it.name == "clean" }.configureEach {
        doLast {
            if (deviceTestManifest.exists() && deviceTestManifest.readText().contains("<!-- Generated by Storytale")) {
                deviceTestManifest.delete()
            }
        }
    }

    AndroidComponentsHelper.onDeviceTestVariant(
        project,
        manifestFile = deviceTestManifest,
        sourcesDir = storytaleBuildSourcesDir,
    ) { variantName, appIdProvider ->
        val generatorTask = createAndroidStorytaleGenerateSourceTask(
            target,
            appIdProvider,
            storytaleBuildSourcesDir,
            storytaleBuildResourcesDir,
            deviceTestManifestFile = deviceTestManifest,
        )

        tasks
            .matching {
                it.name.contains("Manifest", ignoreCase = true) &&
                    (
                        it.name.contains(variantName, ignoreCase = true) ||
                            it.name.contains("DeviceTest", ignoreCase = true) ||
                            it.name.contains("AndroidTest", ignoreCase = true) ||
                            it.name.contains("Storytale", ignoreCase = true)
                        )
            }
            .configureEach {
                dependsOn(generatorTask)
            }

        tasks
            .matching { it.name.endsWith("AarMetadata", ignoreCase = true) }
            .configureEach { enabled = false }

        target.compilations
            .matching {
                it.name == "deviceTest" ||
                    it.name == "androidTest" ||
                    it.name == "debugAndroidTest" ||
                    it.name == StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX
            }
            .configureEach {
                compileTaskProvider.configure {
                    dependsOn(generatorTask)
                    dependsOn(tasks.matching { it.name == "generateResourceAccessorsForCommonStories" })
                }
            }

        val adbPath = AndroidComponentsHelper.resolveAdbPath(project)
        val startEmulatorTask = createStartEmulatorTask(target, adbPath)

        val runTaskName = "${target.name}${StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX}Run"
        if (tasks.findByName(runTaskName) == null) {
            task(runTaskName) {
                group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
                description = "Run Storytale gallery on Android"
                notCompatibleWithConfigurationCache("Launches interactive Android application on device or emulator")

                dependsOn(startEmulatorTask)
                val packageTask = tasks.matching {
                    it.name == "packageAndroidDeviceTest" ||
                        it.name == "packageDebugAndroidTest" ||
                        it.name == "package${variantName.capitalized()}" ||
                        it.name == "assembleAndroidDeviceTest"
                }
                dependsOn(packageTask)

                doLast {
                    val appId = appIdProvider.get()
                    val activityPath = "$appId.StorytaleAppActivity"
                    val activeDevice = AndroidComponentsHelper.resolveActiveDeviceSerial(adbPath)
                    val adbCmd = if (!activeDevice.isNullOrBlank()) {
                        listOf(adbPath, "-s", activeDevice)
                    } else {
                        listOf(adbPath)
                    }

                    val outputsDir = layout.buildDirectory.asFile.get().resolve("outputs/apk")
                    val apkFile = outputsDir.walkTopDown().firstOrNull {
                        it.isFile && it.extension == "apk" && (it.name.contains("test", ignoreCase = true) || it.name.contains(variantName, ignoreCase = true))
                    } ?: outputsDir.walkTopDown().firstOrNull { it.isFile && it.extension == "apk" }

                    if (apkFile != null) {
                        logger.lifecycle("Storytale: Installing APK '${apkFile.name}' via $adbPath on ${activeDevice ?: "default"}...")
                        runProcess(*(adbCmd + listOf("install", "-r", apkFile.absolutePath)).toTypedArray())
                    } else {
                        logger.warn("Storytale: No output APK found in $outputsDir")
                    }

                    logger.lifecycle("Storytale: Launching activity $appId/$activityPath via $adbPath (device: ${activeDevice ?: "default"})")
                    runProcess(*(adbCmd + listOf("shell", "am", "start", "-n", "$appId/$activityPath")).toTypedArray())
                }
            }
        }
    }
}

private fun Project.createAndroidStorytaleGenerateSourceTask(
    target: KotlinTarget,
    appIdProvider: Provider<String>,
    buildSourcesDir: File,
    buildResourcesDir: File,
    deviceTestManifestFile: File? = null,
) = task<AndroidSourceGeneratorTask>("${target.name}${StorytaleGradlePlugin.STORYTALE_GENERATE_SUFFIX}") {
    group = StorytaleGradlePlugin.STORYTALE_TASK_GROUP
    description = "Generate Android source files for '${target.name}'"
    title = target.name
    appPackageName = try {
        appIdProvider.get()
    } catch (e: Throwable) {
        project.logger.debug("Storytale: could not resolve applicationId from provider, using fallback", e)
        AndroidComponentsHelper.resolveFallbackApplicationId(project, target.name)
    }
    outputSourcesDir = buildSourcesDir
    outputResourcesDir = buildResourcesDir
    this.deviceTestManifestFile = deviceTestManifestFile
    dependsOn(tasks.matching { it.name == "generateResourceAccessorsForCommonStories" })
}

private fun Project.createStartEmulatorTask(target: KotlinTarget, adbPath: String): Task {
    return task("${target.name}${StorytaleGradlePlugin.STORYTALE_SOURCESET_SUFFIX}StartEmulator") {
        notCompatibleWithConfigurationCache("Detects or launches external Android emulator daemon")
        doLast {
            val activeDevice = AndroidComponentsHelper.resolveActiveDeviceSerial(adbPath)
            if (activeDevice != null) {
                logger.info("Using active Android device: $activeDevice")
                runProcess(adbPath, "-s", activeDevice, "wait-for-device")
                return@doLast
            }

            val androidHome = System.getenv("ANDROID_HOME") ?: System.getenv("ANDROID_SDK_ROOT")
            val emulatorPath = if (!androidHome.isNullOrBlank()) {
                File(androidHome, "emulator/emulator")
            } else {
                File("emulator")
            }

            if (emulatorPath.isFile && emulatorPath.canExecute()) {
                val output = runProcessIgnoreExit(emulatorPath.absolutePath, "-list-avds")
                val emulatorName = output.trim().lineSequence().lastOrNull { it.isNotBlank() }
                if (emulatorName != null) {
                    logger.info("Starting emulator: $emulatorName")
                    Thread {
                        runProcessIgnoreExit(emulatorPath.absolutePath, "-avd", emulatorName, "-no-snapshot-load")
                    }.start()

                    runProcess(
                        adbPath,
                        "wait-for-device",
                        "shell",
                        "while [[ -z $(getprop sys.boot_completed) ]]; do sleep 1; done;",
                    )
                    return@doLast
                }
            }

            logger.warn("No active Android device detected and no local AVD could be launched.")
        }
    }
}

private fun Project.configureLegacyApplicationDsl(storytaleBuildResourcesDir: File) {
    val androidExt = project.extensions.findByName("android") ?: return
    try {
        val buildTypes = androidExt.javaClass.getMethod("getBuildTypes").invoke(androidExt)
        val createBuildType = buildTypes.javaClass.getMethod("create", String::class.java)
        val storytaleBuildType = createBuildType.invoke(buildTypes, StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX)

        val debugBuildType = buildTypes.javaClass.methods.firstOrNull { it.name == "getByName" && it.parameterCount == 1 }?.invoke(buildTypes, "debug")
        if (debugBuildType != null) {
            val initWithMethod = storytaleBuildType.javaClass.methods.firstOrNull { it.name == "initWith" && it.parameterCount == 1 }
            initWithMethod?.invoke(storytaleBuildType, debugBuildType)
        }

        try {
            val setAppIdSuffix = storytaleBuildType.javaClass.methods.firstOrNull { it.name == "setApplicationIdSuffix" && it.parameterCount == 1 }
            setAppIdSuffix?.invoke(storytaleBuildType, ".${StorytaleGradlePlugin.STORYTALE_EXEC_PREFIX}")
        } catch (e: Throwable) {
            project.logger.debug("Storytale: could not set applicationIdSuffix on Stories buildType", e)
        }

        try {
            val signingConfigs = androidExt.javaClass.getMethod("getSigningConfigs").invoke(androidExt)
            val setSigningConfigMethod = storytaleBuildType.javaClass.methods.firstOrNull {
                it.name == "setSigningConfig" && it.parameterCount == 1
            }
            val configureEachMethod = signingConfigs.javaClass.methods.firstOrNull { it.name == "configureEach" && it.parameterCount == 1 }
            val action = object : Action<Any> {
                override fun execute(config: Any) {
                    val name = config.javaClass.getMethod("getName").invoke(config) as? String
                    if (name == "debug") {
                        setSigningConfigMethod?.invoke(storytaleBuildType, config)
                    }
                }
            }
            configureEachMethod?.invoke(signingConfigs, action)
        } catch (e: Throwable) {
            project.logger.debug("Storytale: could not configure signingConfig on Stories buildType", e)
        }

        try {
            val setMatchingFallbacks = storytaleBuildType.javaClass.methods.firstOrNull {
                it.name == "setMatchingFallbacks" && it.parameterCount == 1 && it.parameterTypes[0] == List::class.java
            } ?: storytaleBuildType.javaClass.methods.firstOrNull {
                it.name == "setMatchingFallbacks" && it.parameterCount == 1 && it.parameterTypes[0] == Array<String>::class.java
            }
            if (setMatchingFallbacks != null) {
                if (setMatchingFallbacks.parameterTypes[0] == List::class.java) {
                    setMatchingFallbacks.invoke(storytaleBuildType, listOf("debug", "release"))
                } else {
                    setMatchingFallbacks.invoke(storytaleBuildType, arrayOf("debug", "release"))
                }
            } else {
                val getMatchingFallbacks = storytaleBuildType.javaClass.methods.firstOrNull { it.name == "getMatchingFallbacks" }
                val list = getMatchingFallbacks?.invoke(storytaleBuildType)
                val addAllMethod = list?.javaClass?.methods?.firstOrNull { it.name == "addAll" && it.parameterCount == 1 }
                addAllMethod?.invoke(list, listOf("debug", "release"))
            }
        } catch (e: Throwable) {
            project.logger.debug("Storytale: could not configure matchingFallbacks on Stories buildType", e)
        }

        try {
            val setDebuggableMethod = storytaleBuildType.javaClass.methods.firstOrNull {
                it.name == "setDebuggable" && it.parameterCount == 1
            }
            setDebuggableMethod?.invoke(storytaleBuildType, true)
        } catch (e: Throwable) {
            project.logger.debug("Storytale: could not set debuggable on Stories buildType", e)
        }

        val sourceSets = androidExt.javaClass.getMethod("getSourceSets").invoke(androidExt)
        val getByNameSourceSet = sourceSets.javaClass.methods.firstOrNull { it.name == "getByName" && it.parameterCount == 1 }
        val storytaleSourceSet = getByNameSourceSet?.invoke(sourceSets, StorytaleGradlePlugin.STORYTALE_EXEC_SUFFIX)
        if (storytaleSourceSet != null) {
            val getManifestMethod = storytaleSourceSet.javaClass.methods.firstOrNull { it.name == "getManifest" }
            val manifest = getManifestMethod?.invoke(storytaleSourceSet)
            val srcFileMethod = manifest?.javaClass?.methods?.firstOrNull { it.name == "srcFile" && it.parameterCount == 1 }
            srcFileMethod?.invoke(manifest, storytaleBuildResourcesDir.resolve("AndroidManifest.xml"))
        }
    } catch (e: Throwable) {
        project.logger.warn("Storytale: could not dynamically configure Android DSL buildTypes/sourceSets", e)
    }
}

