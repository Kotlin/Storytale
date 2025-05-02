package org.jetbrains.compose.storytale.gallery.material3

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.storytale.gallery.compose.LocalIsEmbeddedView
import org.jetbrains.compose.storytale.gallery.ui.theme.LocalCustomDensity

@Composable
fun StorytaleGalleryApp(
    isEmbedded: Boolean = false,
    navHostController: NavHostController = rememberNavController(),
) {
    val isSystemDarkTheme = isSystemInDarkTheme()

    val appState = remember {
        StorytaleGalleryAppState(isSystemDarkTheme)
    }

    CompositionLocalProvider(
        LocalCustomDensity provides Density(LocalDensity.current.density * 0.8f),
        LocalIsEmbeddedView provides isEmbedded,
    ) {
        MaterialTheme(colorScheme = if (appState.isDarkTheme()) darkThemeColors else lightThemeColors) {
            if (isEmbedded) {
                EmbeddedStoryView(appState, navHostController)
            } else {
                FullStorytaleGallery(appState, navHostController)
            }
        }
    }

    LaunchedEffect(isSystemDarkTheme) {
        appState.switchTheme(isSystemDarkTheme)
    }
}

internal val darkThemeColors = darkColorScheme()
internal val lightThemeColors = lightColorScheme()

@Composable
internal fun isDarkMaterialTheme(): Boolean {
    return MaterialTheme.colorScheme == darkThemeColors
}

class StorytaleGalleryAppState(
    initialIsDarkTheme: Boolean,
) {
    private val localIsDarkTheme = mutableStateOf(false)

    init {
        localIsDarkTheme.value = initialIsDarkTheme
    }

    val expandedGroups = mutableStateSetOf<StoryListItemType.Group>()

    fun switchTheme(dark: Boolean) {
        localIsDarkTheme.value = dark
    }

    fun isDarkTheme(): Boolean = localIsDarkTheme.value
}
