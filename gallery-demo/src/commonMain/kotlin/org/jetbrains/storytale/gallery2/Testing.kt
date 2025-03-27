package org.jetbrains.storytale.gallery2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.coroutines.launch
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.story.StoryList
import org.jetbrains.compose.storytale.gallery.story.StoryListItemType
import org.jetbrains.compose.storytale.gallery.story.StorySearchBar
import org.jetbrains.compose.storytale.storiesStorage

@Composable
fun Testing() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val activeStoryItem = remember { mutableStateOf<StoryListItemType.StoryItem?>(null) }

    val filterValue = remember { mutableStateOf("") }

    ResponsiveNavigationDrawer(
        drawerState = drawerState,
        drawerContent = movableContentOf<ColumnScope>{
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                CompositionLocalProvider(LocalDensity provides Density(1.7f)) {
                    StorySearchBar(filterValue.value, { filterValue.value = it })
                }
            }

            val expandedGroups = remember { mutableStateSetOf<StoryListItemType.Group>() }

            val fullList = remember {
                val grouped = storiesStorage.groupBy { it.group }
                grouped.keys.sorted().map { key ->
                    StoryListItemType.Group(key, grouped[key]!!.map { StoryListItemType.StoryItem(it) })
                }
            }
            val filteredList = remember(filterValue.value) {
                if (filterValue.value.length < 2 || filterValue.value.isBlank()) {
                    fullList
                } else {
                    // TODO: currently it reacts on every input change - add debounce?
                    fullList.flatMap { it.children }.filter {
                        val story = (it as? StoryListItemType.StoryItem)?.story
                        story?.name?.contains(filterValue.value, true) == true
                    }
                }
            }

            StoryList(
                activeStory = activeStoryItem.value?.story,
                storyListItems = filteredList,
                expandedGroups = expandedGroups,
                onItemClick = {
                    if (it is StoryListItemType.Group) {
                        if (expandedGroups.contains(it)) {
                            expandedGroups.remove(it)
                        } else {
                            expandedGroups.add(it)
                        }
                    } else if (it is StoryListItemType.StoryItem) {
                        activeStoryItem.value = it
                    }
                }
            )
        },
        content = movableContentOf {
            Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
                GalleryTopAppBar(drawerState, activeStory = activeStoryItem.value?.story)
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val story = activeStoryItem.value?.story
                    story?.content?.invoke(story)
                }
            }
        }
    )
}

@Composable
private fun GalleryTopAppBar(drawerState: DrawerState, activeStory: Story?) {
    val coroutineScope = rememberCoroutineScope()
    val currentWindowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val isExpanded = currentWindowWidthClass == WindowWidthSizeClass.EXPANDED

    CenterAlignedTopAppBar(
        title = {
            AnimatedVisibility(!isExpanded, enter = fadeIn(), exit = fadeOut()) {
                Text(activeStory?.name ?: "")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        navigationIcon = {
            AnimatedVisibility(!isExpanded, enter = fadeIn(), exit = fadeOut()) {
                Row(modifier = Modifier.padding(start = 8.dp)) {
                    FilledTonalIconButton(
                        onClick = {
                            coroutineScope.launch {
                                if (drawerState.isOpen) {
                                    drawerState.close()
                                } else {
                                    drawerState.open()
                                }
                            }
                        },
                    ) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                }
            }
        },
        actions = {

        },
        modifier = Modifier.drawBehind {
            val strokeWidth = 1.dp.toPx()
            val y = size.height
            drawLine(color = Color.Gray, Offset(0f, y), Offset(size.width, y), strokeWidth)
        },
    )
}


@Composable
private fun ResponsiveNavigationDrawer(
    drawerState: DrawerState,
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit
) {
    val currentWindowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val isExpanded = currentWindowWidthClass == WindowWidthSizeClass.EXPANDED

    val drawerModifier =  Modifier.width(280.dp)
    if (!isExpanded) {
        DismissibleNavigationDrawer(drawerContent = {
            DismissibleDrawerSheet(drawerState = drawerState, content = drawerContent, modifier = drawerModifier)
        }, content = content, drawerState = drawerState, gesturesEnabled = false)
    } else {
        PermanentNavigationDrawer(drawerContent = {
            PermanentDrawerSheet(content = drawerContent, modifier = drawerModifier)
        }, content = content)
    }
}
