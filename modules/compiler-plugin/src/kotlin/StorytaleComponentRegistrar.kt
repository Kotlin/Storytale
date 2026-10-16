package org.jetbrains.compose.plugin.storytale.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration

class StorytaleComponentRegistrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean get() = true
    override val pluginId: String = "storytale"

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        Companion.registerExtensions(this)
    }

    companion object {
        fun registerExtensions(extensionStorage: ExtensionStorage) {
            val extension = StorytaleLoweringExtension()
            // In Kotlin 2.4+, ExtensionStorage.registerExtension parameter changed from
            // ProjectExtensionDescriptor to ExtensionPointDescriptor; invoke reflectively for cross-version compatibility
            val companion = IrGenerationExtension::class.java.getField("Companion").get(null)
            val registerMethod = extensionStorage.javaClass.methods.firstOrNull {
                it.name == "registerExtension" && it.parameterCount == 2
            } ?: error("Storytale: Could not resolve registerExtension method on ${extensionStorage.javaClass.name}")
            registerMethod.invoke(extensionStorage, companion, extension)
        }
    }
}
