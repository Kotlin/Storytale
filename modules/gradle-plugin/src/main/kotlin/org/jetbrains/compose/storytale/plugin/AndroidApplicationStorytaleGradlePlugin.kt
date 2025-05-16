package org.jetbrains.compose.storytale.plugin

import org.gradle.api.Project

class AndroidApplicationStorytaleGradlePlugin : StorytaleGradlePlugin() {

    override fun apply(project: Project) {
        project.plugins.withId("com.android.application") {
            project.plugins.withId("org.jetbrains.compose") {
                val extension = project.extensions
                    .create(
                        STORYTALE_EXTENSION_NAME,
                        AndroidStorytaleExtension::class.java,
                        project,
                    )
                project.processConfigurations(extension)
            }
        }
    }
}
