package org.jetbrains.compose.plugin.storytale.compiler

import org.jetbrains.kotlin.backend.common.BodyLoweringPass
import org.jetbrains.kotlin.backend.common.DeclarationTransformer
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.fir.backend.FirMetadataSource
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.text

private val STORYTALE_FQN = FqName("org.jetbrains.compose.storytale")

private class AddCodeSnippetToStoriesLowering(context: IrPluginContext) : BodyLoweringPass {
  val transformer = ReplaceStoryCallWithItsSuccessorWithCodeParameter(context)
  override fun lower(irBody: IrBody, container: IrDeclaration) {
    container.transform(transformer, null)
  }

  class ReplaceStoryCallWithItsSuccessorWithCodeParameter(private val context: IrPluginContext) : IrElementTransformerVoidWithContext() {
    private val STORY_FUNCTION = CallableId(STORYTALE_FQN, Name.identifier("story"))

    override fun visitCall(expression: IrCall): IrExpression {
      val owner = expression.symbol.owner
      if (!owner.isTopLevel || owner.callableId != STORY_FUNCTION) return super.visitCall(expression)

      val callee = expression.getValueArgument(1)
      val storyDescriber = when (callee) {
        is IrFunctionExpression -> callee.function
        else -> error("Unexpected callee: $callee")
      }

      val sourceCode = storyDescriber.getFileSourceCode() ?: return super.visitCall(expression)
      val storyCodeSnippet = sourceCode.substring(storyDescriber.startOffset + 1, storyDescriber.endOffset - 1).trimIndent()

      expression.putValueArgument(0, storyCodeSnippet.toIrConst(context.irBuiltIns.stringType))
      expression.putValueArgument(1, callee)

      return super.visitCall(expression)
    }

    private fun IrDeclaration.getFileSourceCode(): CharSequence? {
      return (file.metadata as FirMetadataSource).fir.source.text
    }
  }
}

private class MentionAllStoriesGettersInsideMainFunctionLowering(
  private val context: IrPluginContext
) : DeclarationTransformer {
  private val allFirstStoriesGetter = mutableListOf<IrStatement>()
  private val alreadyMentionedFiles = mutableSetOf<IrFile>()

  private val STORY_FACTORY_ID = ClassId(STORYTALE_FQN, Name.identifier("StoryDelegate"))

  private val KOTLIN_NATIVE_FQN = FqName("kotlin.native")
  private val HIDE_FROM_OBJC_ID = ClassId(KOTLIN_NATIVE_FQN, Name.identifier("HiddenFromObjC"))

  private val anyType = context.irBuiltIns.anyType
  private val unitType = context.irBuiltIns.unitType
  private val storyFactory = context.referenceClass(STORY_FACTORY_ID)
  private val hiddenFromObjCAnnotation = context.referenceConstructors(HIDE_FROM_OBJC_ID).single()

  override fun transformFlat(declaration: IrDeclaration): List<IrDeclaration>? {
    val factoryType = storyFactory?.defaultType ?: return null

    if (declaration is IrProperty && declaration.isStory(factoryType)) {
      declaration.addHiddenFromObjCAnnotation()
      declaration.mentionGetterInsideMainFunction()
    }


    if (declaration is IrSimpleFunction && declaration.isGeneratedMainViewController()) {
      declaration.addAllMentionedGettersIntoBody()
    }

    return null
  }

  private fun IrProperty.mentionGetterInsideMainFunction() {
    if (parent in alreadyMentionedFiles) return

    val getter = getter!!

    alreadyMentionedFiles.add(parent as IrFile)
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

  private fun IrSimpleFunction.addAllMentionedGettersIntoBody() {
    (body as? IrBlockBody)?.statements?.add(
      0,
      IrObservableBlockImpl(unitType, allFirstStoriesGetter)
    )
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
    AddCodeSnippetToStoriesLowering(pluginContext).lower(moduleFragment)
  }
}