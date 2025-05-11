import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.buildCodeBlock
import org.jetbrains.compose.storytale.plugin.StorytaleGradlePlugin

class PreviewProcessor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val (jetbrains, discarded1) =
            resolver.getSymbolsWithAnnotation("org.jetbrains.compose.ui.tooling.preview.Preview")
                .partition { it.validate() }
        val (androidxDesktop, discarded2) = resolver.getSymbolsWithAnnotation("androidx.compose.desktop.ui.tooling.preview.Preview")
            .partition { it.validate() }

        val (androidxAndroid, discarded3) = resolver.getSymbolsWithAnnotation("androidx.compose.ui.tooling.preview.Preview")
            .partition { it.validate() }

        val validPreviewFunctions = (jetbrains + androidxDesktop + androidxAndroid)
            .filter { it is KSFunctionDeclaration && it.validate() }
            .map { it as KSFunctionDeclaration }


        generatePreviewFile(validPreviewFunctions)

        return discarded1 + discarded2 + discarded3
    }

    private fun generatePreviewFile(previewFunctions: List<KSFunctionDeclaration>) {
        if (previewFunctions.isEmpty()) return

        val packageName = StorytaleGradlePlugin.STORYTALE_PACKAGE
        val fileSpecBuilder = FileSpec.builder(
            packageName, "Previews.story",
        ).apply {
            indent("    ")
            addImport("org.jetbrains.compose.storytale", "story")

            previewFunctions.forEach { function ->
                addImport(function.packageName.asString(), function.simpleName.asString())

                val functionName = function.simpleName.asString()
                val storyName = functionName.removePrefix("Preview").removeSuffix("Preview")

                addProperty(
                    PropertySpec
                        .builder(
                            storyName.ifEmpty { functionName },
                            ClassName("org.jetbrains.compose.storytale", "Story"),
                        )
                        .delegate(
                            buildCodeBlock {
                                beginControlFlow("story")
                                addStatement("%N()", functionName)
                                endControlFlow()
                            },
                        )
                        .build(),
                )
            }
        }

        val containingFiles = previewFunctions
            .mapNotNull { it.containingFile }
            .distinct()

        val dependencies = if (containingFiles.isNotEmpty()) {
            Dependencies(true, containingFiles.first())
        } else {
            Dependencies.ALL_FILES
        }

        val file = codeGenerator.createNewFile(
            dependencies,
            packageName,
            "Previews.story",
        )
        fileSpecBuilder.build().toJavaFileObject().openInputStream().copyTo(file)
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
