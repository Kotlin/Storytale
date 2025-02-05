package org.jetbrains.compose.storytale.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

internal abstract class NativeCopyResourcesTask : DefaultTask() {
  @get:Input
  abstract val xcodeTargetPlatform: Property<String>

  @get:Input
  abstract val xcodeTargetArchs: Property<String>

  @get:PathSensitive(PathSensitivity.ABSOLUTE)
  @get:InputFiles
  abstract val resourceFiles: Property<FileCollection>

  @get:OutputDirectory
  abstract val outputDir: DirectoryProperty

  @TaskAction
  fun run() {
    val outputDir = outputDir.get().asFile
    outputDir.deleteRecursively()
    outputDir.mkdirs()
    logger.info("Clean ${outputDir.path}")

    resourceFiles.get().forEach { dir ->
      if (dir.exists() && dir.isDirectory) {
        logger.info("Copy '${dir.path}' to '${outputDir.path}'")
        dir.walkTopDown().filter { !it.isDirectory && !it.isHidden }.forEach { file ->
          val targetFile = outputDir.resolve(file.relativeTo(dir))
          if (targetFile.exists()) {
            logger.info("Skip [already exists] '${file.path}'")
          } else {
            logger.info(" -> '${file.path}'")
            file.copyTo(targetFile)
          }
        }
      } else {
        logger.info("File '${dir.path}' is not a dir or doesn't exist")
      }
    }
  }
}
