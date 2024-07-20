package org.jetbrains.compose.plugin.storytale.compiler

import org.jetbrains.kotlin.backend.common.DeclarationTransformer
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrAttributeContainer
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.isTopLevel
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

private class MentionAllStoriesGettersInsideMainFunctionLowering(
  private val context: IrPluginContext
) : DeclarationTransformer {
  private val allFirstStoriesGetter = mutableListOf<IrStatement>()
  private val alreadyMentionedFiles = mutableSetOf<IrFile>()

  private val STORYTALE_FQN = FqName("org.jetbrains.compose.storytale")
  private val STORY_FACTORY_ID = ClassId(STORYTALE_FQN, Name.identifier("StoryDelegate"))

  private val KOTLIN_NATIVE_FQN = FqName("kotlin.native")
  private val HIDE_FROM_OBJC_ID = ClassId(KOTLIN_NATIVE_FQN, Name.identifier("HiddenFromObjC"))

  private val storyFactory = context.referenceClass(STORY_FACTORY_ID)
  private val anyType = context.irBuiltIns.anyType
  private val unitType = context.irBuiltIns.unitType
  private val hiddenFromObjCAnnotation = context.referenceConstructors(HIDE_FROM_OBJC_ID).single()

  override fun transformFlat(declaration: IrDeclaration): List<IrDeclaration>? {
    val factoryType = storyFactory?.defaultType ?: return null

    if (declaration is IrProperty && declaration.isStory(factoryType)) {
      declaration.addHiddenFromObjCAnnotation()

      if (declaration.parent !in alreadyMentionedFiles) {
        val getter = declaration.getter!!

        alreadyMentionedFiles.add(declaration.parent as IrFile)
        allFirstStoriesGetter.add(
          IrCallImpl(
            UNDEFINED_OFFSET,
            UNDEFINED_OFFSET,
            getter.returnType,
            getter.symbol,
            0,
            0,
          )
        )
      }
    }

    if (declaration is IrSimpleFunction && declaration.isGeneratedMainViewController()) {
      (declaration.body as? IrBlockBody)?.statements?.add(
        0,
        IrObservableBlockImpl(unitType, allFirstStoriesGetter)
      )
    }

    return null
  }

  private fun IrProperty.isStory(factoryType: IrType) =
    isTopLevel && isDelegated && backingField?.let {
      it.origin == IrDeclarationOrigin.PROPERTY_DELEGATE && it.type == factoryType
    } ?: false

  private fun IrSimpleFunction.isGeneratedMainViewController() =
    isTopLevel && visibility.isPublicAPI &&
      kotlinFqName.asString() == "org.jetbrains.compose.storytale.generated.MainViewController"

  private fun IrDeclaration.addHiddenFromObjCAnnotation() {
    val annotation = IrConstructorCallImpl(
      UNDEFINED_OFFSET,
      UNDEFINED_OFFSET,
      anyType,
      hiddenFromObjCAnnotation,
      0,
      0,
      0
    )
    context.metadataDeclarationRegistrar.addMetadataVisibleAnnotationsToElement(this, annotation)
  }

  private class IrObservableBlockImpl(
    override var type: IrType,
    override val statements: MutableList<IrStatement>
  ) : IrBlock() {
    override val startOffset = UNDEFINED_OFFSET
    override val endOffset = UNDEFINED_OFFSET
    override var origin: IrStatementOrigin? = null
    override var attributeOwnerId: IrAttributeContainer = this
    override var originalBeforeInline: IrAttributeContainer? = null
  }
}

class StorytaleLoweringExtension : IrGenerationExtension {
  override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
    MentionAllStoriesGettersInsideMainFunctionLowering(pluginContext).lower(moduleFragment)
  }
}