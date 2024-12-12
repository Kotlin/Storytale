package org.jetbrains.compose.storytale.gallery.utils

@Throws(ClassCastException::class)
internal inline fun <reified T> Any?.cast(): T = this as T

internal inline fun <reified T> Any?.castOrNull(): T? = this as? T
