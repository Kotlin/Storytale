package org.jetbrains.compose.plugin.storytale

import com.squareup.kotlinpoet.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.cfg.pseudocode.and
import java.io.File
import java.nio.file.Files
import kotlin.io.path.createDirectories

@CacheableTask
open class AndroidSourceGeneratorTask : DefaultTask() {
  @Input
  lateinit var title: String

  @Input
  lateinit var appPackageName: String

  @OutputDirectory
  lateinit var outputResourcesDir: File

  @OutputDirectory
  lateinit var outputSourcesDir: File

  @TaskAction
  fun generate() {
    cleanup(outputSourcesDir)
    cleanup(outputResourcesDir)

    generateSources()
    generateAndroidManifest()
  }

  private fun generateSources() {
      FileSpec.builder(StorytaleGradlePlugin.STORYTALE_PACKAGE, "MainViewController").apply {
        addImport("org.jetbrains.compose.storytale.gallery", "Gallery")

        function("MainViewController") {
          addAnnotation(ClassName("androidx.compose.runtime", "Composable"))
          addStatement("Gallery()")
        }
      }
        .build()
        .writeTo(outputSourcesDir)

    FileSpec.builder(appPackageName, "StorytaleAppActivity").apply {
      addImport("androidx.activity", "enableEdgeToEdge")
      addImport("androidx.activity.compose", "setContent")
      addImport(StorytaleGradlePlugin.STORYTALE_PACKAGE, "MainViewController")

      TypeSpec.classBuilder("StorytaleAppActivity")
        .superclass(ClassName("androidx.activity", "ComponentActivity"))
        .addFunction(
            FunSpec.builder("onCreate")
              .addModifiers(KModifier.OVERRIDE)
              .addParameter("savedInstanceState", ClassName("android.os", "Bundle").copy(nullable = true))
              .addStatement("""
              | super.onCreate(savedInstanceState) 
              | enableEdgeToEdge() 
              | setContent { MainViewController() }
              """.trimMargin())
              .build()
        )
        .build()
        .also(::addType)
    }
      .build()
      .writeTo(outputSourcesDir)

  }

  private fun generateAndroidManifest() {
     val androidManifestFile = File(outputResourcesDir, "AndroidManifest.xml")
     Files.createDirectories(androidManifestFile.parentFile.toPath())
     androidManifestFile.writeText(
      """
        <?xml version="1.0" encoding="utf-8"?>
        <manifest 
            xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:tools="http://schemas.android.com/tools"
            >
            <application
                tools:replace="label, theme"
                android:label="${StorytaleGradlePlugin.STORYTALE_NATIVE_APP_NAME}"
                android:theme="@android:style/Theme.Material.NoActionBar">
                <activity
                    android:name=".stories.StorytaleAppActivity"
                    android:configChanges="orientation|screenSize|screenLayout|keyboardHidden"
                    android:launchMode="singleInstance"
                    android:windowSoftInputMode="adjustPan"
                    android:exported="true">
                    <intent-filter>
                        <action android:name="android.intent.action.MAIN" />
                        <category android:name="android.intent.category.LAUNCHER" />
                    </intent-filter>
                </activity>
            </application>
        
        </manifest>
    """.trimIndent()
    )

  }
}