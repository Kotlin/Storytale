import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.Dynamic
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.buildCodeBlock

/**
 * https://github.com/google/ksp/blob/1db41bc678a1d0e86f71ca3b052a147675769691/examples/playground/test-processor/src/main/kotlin/BuilderProcessor.kt
 */
class PreviewProcessor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols =
            resolver.getSymbolsWithAnnotation("org.jetbrains.compose.ui.tooling.preview.Preview")
        val ret = symbols.filter { !it.validate() }.toList()
        symbols
            .filter { it is KSFunctionDeclaration && it.validate() }
            .forEach { it.accept(BuilderVisitor(), Unit) }
        return ret
    }

    inner class BuilderVisitor : KSVisitorVoid() {
        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            val packageName = function.packageName.asString()
            val fileSpecBuilder = FileSpec.builder(
                MemberName(packageName, "Preview.story"),
            ).apply {
                indent("    ")

                addImport("org.jetbrains.compose.storytale", "story")

                addProperty(
                    PropertySpec
                        .builder(
                            function.simpleName.asString()
                                // because removeSurrounding doesn't work
                                .removePrefix("Preview").removeSuffix("Preview"),
                            ClassName("org.jetbrains.compose.storytale", "Story"),
                        )
                        .delegate(
                            buildCodeBlock {
                                beginControlFlow("story")
                                addStatement("%N()", function.simpleName.asString())
                                endControlFlow()
                            },
                        )
                        .build(),
                )
            }

            val file = codeGenerator.createNewFile(
                Dependencies(true, function.containingFile!!),
                packageName,
                "Previews.story",
            )
            fileSpecBuilder.build().toJavaFileObject().openInputStream().copyTo(file)
        }
    }

    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
            return PreviewProcessor(
                environment.logger,
                environment.codeGenerator,
            )
        }
    }
}
