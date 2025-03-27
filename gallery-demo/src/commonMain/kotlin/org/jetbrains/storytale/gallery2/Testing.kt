package org.jetbrains.storytale.gallery2

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.coroutines.launch
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.story.StoryList
import org.jetbrains.compose.storytale.gallery.story.StoryListItemType
import org.jetbrains.compose.storytale.gallery.story.StoryParametersList2
import org.jetbrains.compose.storytale.gallery.story.StorySearchBar
import org.jetbrains.compose.storytale.gallery.story.code.CodeBlock
import org.jetbrains.compose.storytale.gallery.ui.theme.LocalCustomDensity
import org.jetbrains.compose.storytale.gallery.ui.theme.UseCustomDensity
import org.jetbrains.compose.storytale.storiesStorage

@Composable
fun Testing() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val activeStoryItem = remember {
        mutableStateOf(
            storiesStorage.firstOrNull()?.let { StoryListItemType.StoryItem(it) },
        )
    }

    val filterValue = remember { mutableStateOf("") }

    ResponsiveNavigationDrawer(
        drawerState = drawerState,
        drawerContent = movableContentOf<ColumnScope> {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                UseCustomDensity {
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
                },
            )
        },
        content = movableContentOf {
            Column(modifier = Modifier.fillMaxSize()) {
                GalleryTopAppBar(drawerState, activeStory = activeStoryItem.value?.story)

                HorizontalDivider()

                Row(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainerLowest)) {
                    Column(modifier = Modifier.fillMaxHeight().weight(0.75f)) {
                        Box(modifier = Modifier.fillMaxSize().weight(0.5f), contentAlignment = Alignment.Center) {
                            val story = activeStoryItem.value?.story
                            story?.content?.invoke(story)
                        }
                        HorizontalDivider()
                        Box(modifier = Modifier.fillMaxSize().weight(0.5f)) {
                            val story = activeStoryItem.value?.story
                            CodeBlock(story?.code ?: "", modifier = Modifier.fillMaxSize())
                        }
                    }
                    val storyParameters = activeStoryItem.value?.story?.parameters
                    val hasParameters = storyParameters?.isNotEmpty() == true
                    if (hasParameters) {
                        VerticalDivider()
                        Column(modifier = Modifier.fillMaxHeight().widthIn(max = 250.dp)) {
                            StoryParametersList2(
                                storyParameters!!,
                                modifier = Modifier.fillMaxSize().padding(8.dp).verticalScroll(rememberScrollState(0)),
                            )
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun GalleryTopAppBar(drawerState: DrawerState, activeStory: Story?) {
    val coroutineScope = rememberCoroutineScope()
    val currentWindowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val isExpanded = currentWindowWidthClass == WindowWidthSizeClass.EXPANDED

    CenterAlignedTopAppBar(
        title = {
            AnimatedContent(activeStory?.name) { title ->
                Text(title ?: "")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
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
    )
}


@Composable
private fun ResponsiveNavigationDrawer(
    drawerState: DrawerState,
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    val currentWindowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val isWideWindow = currentWindowWidthClass == WindowWidthSizeClass.EXPANDED

    val drawerModifier = Modifier.width(280.dp)
    if (!isWideWindow) {
        DismissibleNavigationDrawer(
            drawerContent = {
                DismissibleDrawerSheet(drawerState = drawerState, content = drawerContent, modifier = drawerModifier)
            },
            content = content, drawerState = drawerState, gesturesEnabled = false,
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
