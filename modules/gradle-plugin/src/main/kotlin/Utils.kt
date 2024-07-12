package org.jetbrains.compose.plugin.storytale

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


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

inline fun FileSpec.Builder.function(
  name: String,
  builderAction: FunSpec.Builder.() -> Unit
): FunSpec {
  return FunSpec.builder(name).apply(builderAction).build().also {
    addFunction(it)
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
