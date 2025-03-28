package org.jetbrains.compose.storytale.gallery.material3

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.read
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.coroutines.launch
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.story.StoryList
import org.jetbrains.compose.storytale.gallery.story.StoryListItemType
import org.jetbrains.compose.storytale.gallery.story.StorySearchBar
import org.jetbrains.compose.storytale.gallery.ui.theme.UseCustomDensity
import org.jetbrains.compose.storytale.storiesStorage

@Composable
fun FullStorytaleGallery(
    navController: NavHostController = rememberNavController(),
    initialStory: Story? = storiesStorage.firstOrNull(),
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val activeStoryItem = remember {
        mutableStateOf(initialStory?.let { StoryListItemType.StoryItem(it) })
    }

    ResponsiveNavigationDrawer(
        drawerState = drawerState,
        drawerContent = movableContentOf<ColumnScope> {
            DrawerContent(drawerState, navController, activeStoryItem.value)
        },
        content = movableContentOf {
            Column(modifier = Modifier.fillMaxSize()) {
                GalleryTopAppBar(drawerState, activeStory = activeStoryItem.value?.story)

                HorizontalDivider()

                NavHost(
                    navController = navController,
                    startDestination = StoryScreen(activeStoryItem.value?.story?.name ?: ""),
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() },
                    popEnterTransition = { fadeIn() },
                    popExitTransition = { fadeOut() },
                ) {
                    composable<StoryScreen> {
                        val storyName = it.arguments?.read { getString("storyName") } ?: ""
                        StoryContent(activeStoryItem.value?.story, modifier = Modifier.fillMaxSize())

                        SideEffect {
                            activeStoryItem.value = storiesStorage.firstOrNull { it.name == storyName }?.let {
                                StoryListItemType.StoryItem(it)
                            }
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun DrawerContent(
    drawerState: DrawerState,
    navController: NavHostController,
    activeStoryItem: StoryListItemType.StoryItem?,
) {
    val filterValue = remember { mutableStateOf("") }

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

    val coroutineScope = rememberCoroutineScope()

    StoryList(
        activeStory = activeStoryItem?.story,
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
                navController.navigate(StoryScreen(it.story.name))
                coroutineScope.launch {
                    drawerState.close()
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
                        val icon = if (drawerState.isOpen || drawerState.currentOffset > -200f) {
                            MenuOpenImageVector
                        } else {
                            Icons.Default.Menu
                        }
                        AnimatedContent(
                            targetState = icon,
                            transitionSpec = {
                                scaleIn().togetherWith(scaleOut())
                            },
                        ) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    }
                }
            }
        },
        actions = {},
    )
}
