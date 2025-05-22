import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.FirDeclaration
import org.jetbrains.kotlin.fir.declarations.FirDeclarationStatus
import org.jetbrains.kotlin.fir.declarations.FirSimpleFunction
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar
import org.jetbrains.kotlin.fir.extensions.FirStatusTransformerExtension
import org.jetbrains.kotlin.fir.extensions.transform
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

class MakePreviewPublicFirExtensionRegistrar : FirExtensionRegistrar() {
    companion object {
        private val PREVIEW_ANNOTATION_FQ_NAME = FqName("org.jetbrains.compose.ui.tooling.preview.Preview")
        private val ANDROIDX_PREVIEW_ANNOTATION_FQ_NAME = FqName("androidx.compose.ui.tooling.preview.Preview")
        private val DESKTOP_PREVIEW_ANNOTATION_FQ_NAME = FqName("androidx.compose.desktop.ui.tooling.preview.Preview")

        private val PREVIEW_CLASS_IDS = setOf(
            ClassId.topLevel(PREVIEW_ANNOTATION_FQ_NAME),
            ClassId.topLevel(ANDROIDX_PREVIEW_ANNOTATION_FQ_NAME),
            ClassId.topLevel(DESKTOP_PREVIEW_ANNOTATION_FQ_NAME),
        )
    }

    override fun ExtensionRegistrarContext.configurePlugin() {
        +FirStatusTransformerExtension.Factory { session -> Extension(session) }
    }

    class Extension(session: FirSession) : FirStatusTransformerExtension(session) {
        override fun needTransformStatus(declaration: FirDeclaration): Boolean {
            if (declaration !is FirSimpleFunction) return false
            return PREVIEW_CLASS_IDS.any { declaration.hasAnnotation(it, session) }
        }

        override fun transformStatus(
            status: FirDeclarationStatus,
            function: FirSimpleFunction,
            containingClass: FirClassLikeSymbol<*>?,
            isLocal: Boolean,
        ): FirDeclarationStatus {
            return status.transform(visibility = Visibilities.Public)
        }
    }
}
