package org.jetbrains.storytale.gallery2

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.read
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.story.StoryList
import org.jetbrains.compose.storytale.gallery.story.StoryListItemType
import org.jetbrains.compose.storytale.gallery.story.StoryParametersList2
import org.jetbrains.compose.storytale.gallery.story.StorySearchBar
import org.jetbrains.compose.storytale.gallery.story.code.CodeBlock
import org.jetbrains.compose.storytale.gallery.ui.component.IconButton
import org.jetbrains.compose.storytale.gallery.ui.theme.UseCustomDensity
import org.jetbrains.compose.storytale.storiesStorage

@Serializable
data class StoryScreen(val storyName: String)

@Composable
fun Testing(
    navController: NavHostController = rememberNavController(),
    initialStory: Story? = storiesStorage.firstOrNull(),
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val activeStoryItem = remember {
        mutableStateOf(initialStory?.let { StoryListItemType.StoryItem(it) })
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

            val coroutineScope = rememberCoroutineScope()

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
                        navController.navigate(StoryScreen(it.story.name))
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                },
            )
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
fun EmbeddedStoryView(
    activeStory: Story?
) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            activeStory?.name ?: "",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(12.dp),
        )
        StoryContent(activeStory, useEmbeddedView = true, modifier = Modifier.weight(1f))
        HorizontalDivider()
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 12.dp)) {
            Text(
                "Powered by Storytale",
                modifier = Modifier.align(Alignment.CenterEnd),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 9.sp,
            )
        }
    }
}

@Composable
private fun StoryContent(
    activeStory: Story?,
    useEmbeddedView: Boolean = false,
    modifier: Modifier = Modifier
) {
    val widthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val heightClass = currentWindowAdaptiveInfo().windowSizeClass.windowHeightSizeClass

    val isSmallHeight = heightClass == WindowHeightSizeClass.COMPACT
    val isSmallWidth = widthClass == WindowWidthSizeClass.COMPACT

    Box(modifier = modifier) {
        val showOverlayParameters = remember { mutableStateOf(false) }

        val previewContent = @Composable {
            Box(modifier = Modifier.fillMaxSize()) {

                Box(
                    modifier = Modifier.fillMaxSize().horizontalScroll(rememberScrollState(0)),
                    contentAlignment = Alignment.Center,
                ) {
                    activeStory?.content?.invoke(activeStory)
                }

                AnimatedVisibility(
                    (isSmallWidth || useEmbeddedView) && activeStory?.parameters?.isNotEmpty() == true,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.BottomEnd),
                ) {
                    SmallFloatingActionButton(
                        onClick = {
                            showOverlayParameters.value = true
                        },
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Icon(imageVector = Icons.Default.Settings, null)
                    }
                }
            }
        }

        val codeContent = movableContentOf<BoxScope> { boxScope ->
            with(boxScope) {
                CodeBlock(activeStory?.code ?: "", modifier = Modifier.fillMaxSize())
                val clipboard = LocalClipboard.current
                val coroutineScope = rememberCoroutineScope()
                SmallFloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            // TODO: add expect / actual for ClipEntry creation
                            //clipboard.setClipEntry(ClipEntry())
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                ) {
                    Icon(imageVector = ContentCopyImageVector, null)
                }
            }
        }

        AnimatedContent(
            targetState = isSmallHeight || useEmbeddedView,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
        ) { isSmall ->
            if (isSmall) {
                val selectedTabIndex = remember { mutableStateOf(0) }
                Column(modifier = Modifier.fillMaxSize()) {
                    StoryTabs(
                        modifier = Modifier.fillMaxWidth(),
                        onPreviewTabClicked = { selectedTabIndex.value = 0 },
                        onCodeTabClicked = { selectedTabIndex.value = 1 },
                    )

                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
                        contentAlignment = Alignment.Center,
                    ) {
                        when (selectedTabIndex.value) {
                            0 -> previewContent()
                            1 -> codeContent(this)
                        }
                    }
                }
            } else {
                StoryPreviewAndCodeCombined(
                    activeStory = activeStory,
                    previewContent = { previewContent() },
                    codeContent = codeContent,
                    modifier = Modifier,
                )
            }
        }

        OverlayParametersList(activeStory, showOverlayParameters)
    }
}

@Composable
private fun BoxScope.OverlayParametersList(
    activeStory: Story?,
    showOverlayParameters: MutableState<Boolean>,
) {
    AnimatedVisibility(visible = showOverlayParameters.value, enter = fadeIn(), exit = fadeOut()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.15f))
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        val down = awaitFirstDown()
                        showOverlayParameters.value = false
                    }
                },
        )
    }

    AnimatedVisibility(
        modifier = Modifier.align(Alignment.TopEnd),
        visible = showOverlayParameters.value,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it }),
    ) {
        Surface(modifier = Modifier.fillMaxHeight().width(250.dp), shadowElevation = 16.dp) {
            Column(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainerLowest),
            ) {
                IconButton(
                    onClick = {
                        showOverlayParameters.value = false
                    },
                    modifier = Modifier.align(Alignment.Start).padding(8.dp),
                ) {
                    Icon(imageVector = Icons.Default.Close, null)
                }
                StoryParametersList2(
                    activeStory?.parameters ?: emptyList(),
                    modifier = Modifier.fillMaxSize().padding(8.dp).verticalScroll(rememberScrollState(0)),
                )
            }
        }
    }
}

@Composable
private fun StoryPreviewAndCodeCombined(
    activeStory: Story?,
    previewContent: @Composable () -> Unit,
    codeContent: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    val widthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val isSmallWidth = widthClass == WindowWidthSizeClass.COMPACT

    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Column(modifier = Modifier.fillMaxHeight().weight(0.75f)) {
            Box(modifier = Modifier.fillMaxSize().weight(0.5f), contentAlignment = Alignment.Center) {
                previewContent()
            }
            HorizontalDivider()
            Box(modifier = Modifier.fillMaxSize().weight(0.5f)) {
                codeContent(this)
            }
        }

        val storyParameters = activeStory?.parameters
        val hasParameters = activeStory?.parameters?.isNotEmpty() == true

        if (!isSmallWidth && hasParameters) {
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

@Composable
private fun StoryTabs(
    modifier: Modifier = Modifier,
    onPreviewTabClicked: () -> Unit = {},
    onCodeTabClicked: () -> Unit = {},
) {
    val selectedTabIndex = remember { mutableStateOf(0) }
    val selectedTextColor = MaterialTheme.colorScheme.onSurface
    val unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    SecondaryTabRow(selectedTabIndex = selectedTabIndex.value, modifier = modifier) {
        Tab(
            selected = selectedTabIndex.value == 0,
            onClick = {
                selectedTabIndex.value = 0
                onPreviewTabClicked()
            },
            modifier = Modifier.height(48.dp),
        ) {
            val textColor = if (selectedTabIndex.value == 0) selectedTextColor else unselectedTextColor
            Text(text = "Preview", style = MaterialTheme.typography.titleSmall, color = textColor)
        }
        Tab(
            selected = selectedTabIndex.value == 1,
            onClick = {
                selectedTabIndex.value = 1
                onCodeTabClicked()
            },
        ) {
            val textColor = if (selectedTabIndex.value == 1) selectedTextColor else unselectedTextColor
            Text(text = "Code", style = MaterialTheme.typography.titleSmall, color = textColor)
        }
    }
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
    val isSmallWindow = currentWindowWidthClass == WindowWidthSizeClass.COMPACT

    val drawerModifier = Modifier.width(280.dp)
    if (!isWideWindow) {
        DismissibleNavigationDrawer(
            drawerContent = {
                DismissibleDrawerSheet(drawerState = drawerState, content = drawerContent, modifier = drawerModifier)
            },
            content = content, drawerState = drawerState, gesturesEnabled = isSmallWindow,
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
