package org.jetbrains.compose.plugin.storytale

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import java.io.File

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
