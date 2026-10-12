package org.jetbrains.compose.storytale.plugin

import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalWasmDsl::class)
class ResolveScriptFileNameTest {

    @Test
    fun testResolveScriptFileName_composeApp() {
        val project = ProjectBuilder.builder().withName("composeApp").build()
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")
        val kotlin = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
        val target = kotlin.wasmJs() as KotlinJsIrTarget

        val scriptName = project.resolveScriptFileName(target)
        assertEquals("composeApp.js", scriptName)
    }

    @Test
    fun testResolveScriptFileName_shared() {
        val project = ProjectBuilder.builder().withName("shared").build()
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")
        val kotlin = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
        val target = kotlin.wasmJs() as KotlinJsIrTarget

        val scriptName = project.resolveScriptFileName(target)
        assertEquals("shared.js", scriptName)
    }

    @Test
    fun testResolveScriptFileName_nestedAppShared() {
        val root = ProjectBuilder.builder().withName("root").build()
        val app = ProjectBuilder.builder().withName("app").withParent(root).build()
        val shared = ProjectBuilder.builder().withName("shared").withParent(app).build()
        shared.plugins.apply("org.jetbrains.kotlin.multiplatform")
        val kotlin = shared.extensions.getByType(KotlinMultiplatformExtension::class.java)
        val target = kotlin.wasmJs() as KotlinJsIrTarget

        val scriptName = shared.resolveScriptFileName(target)
        assertEquals("root-app-shared.js", scriptName)
    }

    @Test
    fun testResolveScriptFileName_outputModuleNameOverride() {
        val project = ProjectBuilder.builder().withName("shared").build()
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")
        val kotlin = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
        val target = kotlin.wasmJs() as KotlinJsIrTarget
        target.outputModuleName.set("customModule")

        val scriptName = project.resolveScriptFileName(target)
        assertEquals("customModule.js", scriptName)
    }

    @Test
    fun testResolveScriptFileName_webpackTaskMainOutputFileName() {
        val project = ProjectBuilder.builder().withName("shared").build()
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")
        val kotlin = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
        val target = kotlin.wasmJs {
            browser()
        } as KotlinJsIrTarget

        val compilation = target.compilations.getByName("main")
        project.tasks.register("wasmJsStoriesDevelopmentWebpack", KotlinWebpack::class.java, compilation).configure {
            mainOutputFileName.set("customBundle.js")
        }

        val scriptName = project.resolveScriptFileName(target)
        assertEquals("customBundle.js", scriptName)
    }
}
