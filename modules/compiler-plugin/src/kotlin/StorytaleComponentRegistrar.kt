package org.jetbrains.compose.plugin.storytale.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration

class StorytaleComponentRegistrar : CompilerPluginRegistrar() {
  override val supportsK2: Boolean get() = true

  override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
    Companion.registerExtensions(this)
  }

  companion object {
    fun registerExtensions(extensionStorage: ExtensionStorage) = with(extensionStorage) {
      IrGenerationExtension.registerExtension(StorytaleLoweringExtension())
    }
  }
}
