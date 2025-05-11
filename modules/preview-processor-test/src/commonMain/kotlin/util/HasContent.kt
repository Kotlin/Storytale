package util

import org.intellij.lang.annotations.Language

@Suppress("NOTHING_TO_INLINE")
inline infix fun String.hasContent(@Language("kotlin") content: String): Pair<String, String> {
    return this to content
}
