package org.jetbrains.compose.storytale.gallery.material3

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun ResponsiveNavigationDrawer(
    drawerState: DrawerState,
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    val currentWindowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val isWideWindow = currentWindowWidthClass == WindowWidthSizeClass.EXPANDED
    val isSmallWindow = currentWindowWidthClass == WindowWidthSizeClass.COMPACT

    val drawerModifier = Modifier.width(280.dp)
    if (!isWideWindow) {
        DismissibleNavigationDrawer(
            drawerContent = remember {
                movableContentOf {
                    DismissibleDrawerSheet(
                        drawerState = drawerState,
                        content = drawerContent,
                        modifier = drawerModifier,
                    )
                }
            },
            content = content,
            drawerState = drawerState,
            gesturesEnabled = isSmallWindow,
        )
    } else {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(content = drawerContent, modifier = drawerModifier)
            },
            content = content,
        )
    }
}
