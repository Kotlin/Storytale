package org.jetbrains.compose.storytale.gallery.material3

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.coroutines.launch
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.compose.LocalIsEmbeddedView
import org.jetbrains.compose.storytale.gallery.ui.theme.UseCustomDensity
import org.jetbrains.compose.storytale.storiesStorage

@Composable
fun FullStorytaleGallery(
    appState: StorytaleGalleryAppState,
    navController: NavHostController = rememberNavController(),
    initialStory: Story? = storiesStorage.firstOrNull(),
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val activeStoryItem = remember {
        mutableStateOf(initialStory?.let { StoryListItemType.StoryItem(it) })
    }

    LaunchedEffect(Unit) {
        navController.currentBackStack.collect { backstack ->
            val entry = backstack.lastOrNull()
            val isStory = entry?.destination?.route?.startsWith("story/") == true
            if (entry != null && isStory) {
                val story = entry.toRoute<StoryScreen>()
                activeStoryItem.value = storiesStorage.firstOrNull {
                    it.name == story.storyName
                }?.let {
                    StoryListItemType.StoryItem(it)
                }
            }
        }
    }

    ResponsiveNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(appState, drawerState, navController, activeStoryItem.value)
        },
        content = {
            NavHost(
                navController = navController,
                startDestination = StoryScreen(activeStoryItem.value?.story?.name ?: ""),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                composable<StoryScreen> {
                    val showOverlayParameters = rememberSaveable { mutableStateOf(false) }
                    Column(modifier = Modifier.fillMaxSize()) {
                        GalleryTopAppBar(
                            appState = appState,
                            drawerState = drawerState,
                            activeStory = activeStoryItem.value?.story,
                            showOverlayParameters = showOverlayParameters,
                        )
                        HorizontalDivider()
                        StoryContent(
                            activeStory = activeStoryItem.value?.story,
                            showOverlayParameters = showOverlayParameters,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun DrawerContent(
    appState: StorytaleGalleryAppState,
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
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = filterValue.value,
                onValueChange = { filterValue.value = it },
                maxLines = 1,
                label = { Text("Type to filter") },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
            )
        }
    }

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
        expandedGroups = appState.expandedGroups,
        onItemClick = {
            if (it is StoryListItemType.Group) {
                if (appState.expandedGroups.contains(it)) {
                    appState.expandedGroups.remove(it)
                } else {
                    appState.expandedGroups.add(it)
                }
            } else if (it is StoryListItemType.StoryItem) {
                if (activeStoryItem?.story?.name != it.story.name) {
                    navController.navigate(StoryScreen(it.story.name))

                    coroutineScope.launch {
                        drawerState.close()
                    }
                }
            }
        },
    )
}

@Composable
private fun GalleryTopAppBar(
    drawerState: DrawerState,
    appState: StorytaleGalleryAppState,
    activeStory: Story?,
    showOverlayParameters: MutableState<Boolean>,
) {
    val activeStoryName = activeStory?.name.orEmpty()
    val coroutineScope = rememberCoroutineScope()
    val currentWindowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val isExpanded = currentWindowWidthClass == WindowWidthSizeClass.EXPANDED

    CenterAlignedTopAppBar(
        title = {
            AnimatedContent(activeStoryName) { title ->
                Text(title)
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
        actions = {
            val widthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

            val isSmallWidth = widthClass == WindowWidthSizeClass.COMPACT
            val useEmbeddedView = LocalIsEmbeddedView.current

            ThemeSwitcherIconButton(appState)
            AnimatedVisibility(
                (isSmallWidth || useEmbeddedView) && activeStory?.parameters?.isNotEmpty() == true,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                IconButton(
                    onClick = {
                        showOverlayParameters.value = true
                    },
                ) {
                    Icon(imageVector = Icons.Default.Settings, null)
                }
            }
        },
    )
}

@Composable
internal fun ThemeSwitcherIconButton(appState: StorytaleGalleryAppState) {
    IconButton(
        onClick = {
            appState.switchTheme(!appState.isDarkTheme())
        },
    ) {
        AnimatedContent(targetState = appState.isDarkTheme()) { isDarkTheme ->
            if (isDarkTheme) {
                Icon(imageVector = Light_mode, contentDescription = null)
            } else {
                Icon(imageVector = Dark_mode, contentDescription = null)
            }
        }
    }
}
