package org.jetbrains.compose.plugin.storytale

import com.squareup.kotlinpoet.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.cfg.pseudocode.and
import java.io.File

@CacheableTask
open class AndroidSourceGeneratorTask : DefaultTask() {
  @Input
  lateinit var title: String

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
    val file = FileSpec.builder(StorytaleGradlePlugin.STORYTALE_PACKAGE, "Main").apply {
      addImport("androidx.activity", "ComponentActivity")
      addImport("androidx.activity", "enableEdgeToEdge")
      addImport("androidx.activity.compose", "setContent")

      addAnnotation(ClassName("androidx.compose.runtime", "Composable"))
      function("MainViewController") {
        addStatement("Gallery()")
      }

      TypeSpec.classBuilder("AppActivity")
        .addFunction(
            FunSpec.builder("onCreate")
              .addModifiers(KModifier.OVERRIDE)
              .addParameter("savedInstanceState", ClassName("android.os", "Bundle"))
              .addStatement("""
              | super.onCreate(savedInstanceState) 
              | enableEdgeToEdge() 
              | setContent { MainViewController() }
              """.trimMargin())
              .build()
        )
        .build()
        .also(::addType)
    }.build()

    file.writeTo(outputSourcesDir)
  }

  private fun generateAndroidManifest() {
     val androidManifestFile = File(outputResourcesDir, "AndroidManifest.xml")
     androidManifestFile.writeText(
      """
        <?xml version="1.0" encoding="utf-8"?>
        <manifest xmlns:android="http://schemas.android.com/apk/res/android">
            <application
                android:icon="@android:drawable/ic_menu_compass"
                android:label="${StorytaleGradlePlugin.STORYTALE_NATIVE_APP_NAME}"
                android:theme="@android:style/Theme.Material.NoActionBar">
                <activity
                    android:name="${StorytaleGradlePlugin.STORYTALE_PACKAGE}.AppActivity"
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