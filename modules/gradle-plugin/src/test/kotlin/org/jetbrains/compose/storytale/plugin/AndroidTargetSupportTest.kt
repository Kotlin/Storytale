package org.jetbrains.compose.storytale.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AndroidTargetSupportTest {

    @Test
    fun testAndroidApplicationConfiguration() {
        val project = ProjectBuilder.builder().withName("appTest").build()
        project.plugins.apply("com.android.application")
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")
        project.plugins.apply("org.jetbrains.kotlin.plugin.compose")
        project.plugins.apply("org.jetbrains.compose")
        project.plugins.apply("org.jetbrains.compose.storytale")

        val android = project.extensions.getByName("android")
        android.javaClass.getMethod("setNamespace", String::class.java).invoke(android, "org.storytale.test.app")
        android.javaClass.getMethod("setCompileSdkVersion", Int::class.javaPrimitiveType).invoke(android, 34)

        val kotlin = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
        kotlin.androidTarget()

        (project as ProjectInternal).evaluate()

        // 1. Android DSL buildType verification
        val buildTypes = android.javaClass.getMethod("getBuildTypes").invoke(android)
        val storiesBuildType = buildTypes.javaClass.getMethod("findByName", String::class.java).invoke(buildTypes, "Stories")
        assertNotNull("Stories buildType should be registered in android DSL", storiesBuildType)
        val isDebuggable = storiesBuildType!!.javaClass.getMethod("isDebuggable").invoke(storiesBuildType) as Boolean
        assertTrue("Stories buildType should be configured as debuggable", isDebuggable)

        val sourceSet = project.kotlinExtension.sourceSets.findByName("androidStories")
        assertNotNull("androidStories source set should be created for android application", sourceSet)
        assertTrue(
            "androidStories source set should include generated sources directory",
            sourceSet!!.kotlin.srcDirs.any { it.path.contains("sources") },
        )
        val storyExtension = project.extensions.getByType(StorytaleExtension::class.java)
        assertTrue(
            "androidStories source set should include mainStoriesSourceSet srcDirs",
            sourceSet.kotlin.srcDirs.containsAll(storyExtension.mainStoriesSourceSet.kotlin.srcDirs),
        )

        // 3. Task graph registration and metadata
        val generateTask = project.tasks.findByName("androidStorytaleGenerate") as? AndroidSourceGeneratorTask
        assertNotNull("androidStorytaleGenerate task should be registered", generateTask)
        assertEquals("android", generateTask!!.title)
        assertTrue("outputSourcesDir must be configured", generateTask.outputSourcesDir.path.contains("sources"))
        assertTrue("outputResourcesDir must be configured", generateTask.outputResourcesDir.path.contains("resources"))

        val runTask = project.tasks.findByName("androidStoriesRun")
        assertNotNull("androidStoriesRun task should be registered for application target", runTask)
        assertEquals(StorytaleGradlePlugin.STORYTALE_TASK_GROUP, runTask!!.group)
    }

    @Test
    fun testAndroidApplicationMinSdkFloorConfigured() {
        val project = ProjectBuilder.builder().withName("minSdkTest").build()
        project.plugins.apply("com.android.application")
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")
        project.plugins.apply("org.jetbrains.kotlin.plugin.compose")
        project.plugins.apply("org.jetbrains.compose")
        project.plugins.apply("org.jetbrains.compose.storytale")

        val android = project.extensions.getByName("android")
        android.javaClass.getMethod("setNamespace", String::class.java).invoke(android, "org.storytale.test.minsdk")
        android.javaClass.getMethod("setCompileSdkVersion", Int::class.javaPrimitiveType).invoke(android, 34)

        val defaultConfig = android.javaClass.getMethod("getDefaultConfig").invoke(android)
        defaultConfig.javaClass.getMethod("setMinSdkVersion", Int::class.javaPrimitiveType).invoke(defaultConfig, 24)

        val androidComponents = project.extensions.getByType(com.android.build.api.variant.ApplicationAndroidComponentsExtension::class.java)
        var storiesMinSdk: Int? = null
        androidComponents.onVariants(androidComponents.selector().withBuildType("Stories")) { variant ->
            storiesMinSdk = variant.minSdk.apiLevel
        }

        val kotlin = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
        kotlin.androidTarget()

        (project as ProjectInternal).evaluate()

        assertNotNull("Stories variant should be evaluated", storiesMinSdk)
        assertEquals("Stories variant minSdk must be raised to at least 30", 30, storiesMinSdk)
    }

    @Test
    fun testAndroidLibraryConfiguration() {
        val project = ProjectBuilder.builder().withName("libTest").build()
        project.plugins.apply("com.android.library")
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")
        project.plugins.apply("org.jetbrains.kotlin.plugin.compose")
        project.plugins.apply("org.jetbrains.compose")
        project.plugins.apply("org.jetbrains.compose.storytale")

        val android = project.extensions.getByName("android")
        android.javaClass.getMethod("setNamespace", String::class.java).invoke(android, "org.storytale.test.lib")
        android.javaClass.getMethod("setCompileSdkVersion", Int::class.javaPrimitiveType).invoke(android, 34)

        val kotlin = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
        kotlin.androidTarget()
        val androidTest = kotlin.sourceSets.create("androidTest")

        (project as ProjectInternal).evaluate()

        val runTask = project.tasks.findByName("androidStoriesRun")
        assertNotNull("androidStoriesRun task should be registered for android library target", runTask)
        assertEquals(StorytaleGradlePlugin.STORYTALE_TASK_GROUP, runTask!!.group)

        val generateTask = project.tasks.findByName("androidStorytaleGenerate") as? AndroidSourceGeneratorTask
        assertNotNull("androidStorytaleGenerate task should be registered for library", generateTask)
        assertNotNull("deviceTestManifestFile should be configured for library module", generateTask!!.deviceTestManifestFile)

        assertNotNull("androidTest source set should exist for library target", androidTest)
        assertTrue(
            "androidTest source set should include generated storytale sources",
            androidTest.kotlin.srcDirs.any { it.path.contains("sources") },
        )
        val storyExtension = project.extensions.getByType(StorytaleExtension::class.java)
        assertTrue(
            "androidTest source set should include mainStoriesSourceSet srcDirs",
            androidTest.kotlin.srcDirs.containsAll(storyExtension.mainStoriesSourceSet.kotlin.srcDirs),
        )
    }

    @Test
    fun testAndroidKotlinMultiplatformLibraryConfiguration() {
        val project = ProjectBuilder.builder().withName("kmpLibTest").build()
        project.plugins.apply("com.android.kotlin.multiplatform.library")
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")
        project.plugins.apply("org.jetbrains.kotlin.plugin.compose")
        project.plugins.apply("org.jetbrains.compose")
        project.plugins.apply("org.jetbrains.compose.storytale")

        val kotlin = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
        val androidLib = kotlin.extensions.findByName("androidLibrary")
        assertNotNull("androidLibrary extension should exist on kotlin", androidLib)
        val setNamespaceMethod = androidLib!!.javaClass.methods.firstOrNull { it.name == "setNamespace" && it.parameterCount == 1 }
        setNamespaceMethod?.invoke(androidLib, "org.storytale.test.kmplib")
        val setCompileSdkMethod = androidLib.javaClass.methods.firstOrNull {
            (it.name == "setCompileSdk" || it.name == "setCompileSdkVersion") && it.parameterCount == 1
        }
        setCompileSdkMethod?.invoke(androidLib, 34)

        (project as ProjectInternal).evaluate()

        val runTask = project.tasks.findByName("androidStoriesRun")
            ?: project.tasks.firstOrNull { it.name.endsWith("StoriesRun") }
        assertNotNull("StoriesRun task should be registered for KMP library target", runTask)
        assertEquals(StorytaleGradlePlugin.STORYTALE_TASK_GROUP, runTask!!.group)

        val generateTask = project.tasks.firstOrNull { it.name.endsWith("StorytaleGenerate") }
        assertNotNull("StorytaleGenerate task should be registered for KMP library target", generateTask)
    }

    @Test
    fun testAndroidComponentsHelperResolution() {
        val project = ProjectBuilder.builder().withName("helperTest").build()
        project.plugins.apply("com.android.application")
        val android = project.extensions.getByName("android")
        android.javaClass.getMethod("setNamespace", String::class.java).invoke(android, "org.storytale.helper.app")

        val adbPath = AndroidComponentsHelper.resolveAdbPath(project)
        assertTrue("Adb path should be resolved", adbPath.isNotBlank())

        val fallbackAppId = AndroidComponentsHelper.resolveFallbackApplicationId(project, "Stories")
        assertEquals("Fallback application ID should use namespace and variant", "org.storytale.helper.app.Stories", fallbackAppId)
    }
}
