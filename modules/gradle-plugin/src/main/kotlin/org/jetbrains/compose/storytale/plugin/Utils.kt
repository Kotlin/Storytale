@file:OptIn(org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi::class)
@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package org.jetbrains.compose.storytale.plugin

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberSpecHolder
import com.squareup.kotlinpoet.TypeSpec
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.zip.ZipInputStream
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.internal
import org.jetbrains.kotlin.gradle.plugin.mpp.publishing.configureResourcesPublicationAttributes
import org.jetbrains.kotlin.gradle.plugin.mpp.resources.resolve.ResolveResourcesFromDependenciesTask
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget

fun cleanup(file: File) {
    if (file.exists()) {
        val listing = file.listFiles()
        if (listing != null) {
            for (sub in listing) {
                cleanup(sub)
            }
        }
        file.delete()
    }
}

inline fun MemberSpecHolder.Builder<*>.function(
    name: String,
    builderAction: FunSpec.Builder.() -> Unit,
): FunSpec {
    return FunSpec.builder(name).apply(builderAction).build().also {
        addFunction(it)
    }
}

inline fun FileSpec.Builder.klass(
    name: String,
    builderAction: TypeSpec.Builder.() -> Unit,
): TypeSpec {
    return TypeSpec.classBuilder(name).apply(builderAction).build().also {
        addType(it)
    }
}

fun StorytaleExtension.getBuildDirectory(target: KotlinTarget) = with(project) {
    file(buildDir.resolve(this@getBuildDirectory.buildDir).resolve(name).resolve(target.name))
}

abstract class UnzipResourceTask : DefaultTask() {
    @get:Input
    abstract val resourcePath: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun unzip() {
        val resourcePath = resourcePath.get()
        javaClass.classLoader.getResourceAsStream(resourcePath)?.use { zipStream ->
            unzipStream(zipStream)
        } ?: throw IOException("Resource not found: $resourcePath")
    }

    private fun unzipStream(zipStream: InputStream) {
        val outputDir = outputDir.get()

        ZipInputStream(zipStream).use { zis ->
            while (true) {
                val entry = zis.nextEntry ?: break
                val newFile = File(outputDir.asFile, entry.name)
                if (entry.isDirectory) {
                    newFile.mkdirs()
                } else {
                    newFile.parentFile.mkdirs()
                    Files.copy(zis, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                }
                zis.closeEntry()
            }
        }
    }
}

val KotlinCompilation<*>.resolveDependencyResourcesTaskName: String
    get() = "${name.lowercase()}${target.name.capitalized()}ResolveDependencyResources"

fun setupResourceResolvingForTarget(storytaleBuildDir: File, compilation: KotlinCompilation<*>) {
    compilation.project.tasks.register(
        compilation.resolveDependencyResourcesTaskName,
        ResolveResourcesFromDependenciesTask::class.java,
        Action {
            filterResourcesByExtension.set(true)
            archivesFromDependencies.setFrom(getArchivesFromResources(compilation))
            outputDirectory.set(storytaleBuildDir.resolve("resolved-dependency-resources"))
        },
    )
}

fun getArchivesFromResources(compilation: KotlinCompilation<*>): FileCollection {
    val dependenciesConfiguration = if (compilation.target is KotlinJsIrTarget) {
        compilation.internal.configurations.runtimeDependencyConfiguration
            ?: return compilation.project.files()
    } else {
        compilation.internal.configurations.compileDependencyConfiguration
    }

    return dependenciesConfiguration.incoming.artifactView {
        withVariantReselection()
        attributes {
            configureResourcesPublicationAttributes(compilation.target)
        }
        isLenient = true
    }.files
}

fun Project.execute(vararg args: String): String = ByteArrayOutputStream().apply {
    exec {
        commandLine(*args)
        standardOutput = this@apply
    }
}.toString()
