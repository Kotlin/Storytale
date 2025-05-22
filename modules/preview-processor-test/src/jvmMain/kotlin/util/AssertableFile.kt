package util

import java.io.File
import org.assertj.core.util.Files
import org.intellij.lang.annotations.Language

data class AssertableFile(
    val path: String,
    val content: String,
) {
    override fun toString(): String {
        return buildString {
            append('\"')
            append(path)
            appendLine("\" has content \"\"\"")
            appendLine(content)
            appendLine("\"\"\"")
        }
    }
}

fun File.assertable(): AssertableFile {
    val it = this
    return path hasContent Files.contentOf(it, Charsets.UTF_8).trim()
}

@Suppress("NOTHING_TO_INLINE")
inline infix fun String.hasContent(@Language("kotlin") content: String): AssertableFile {
    return AssertableFile(this, content)
}
