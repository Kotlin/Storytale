package org.jetbrains.compose.storytale.gallery.material3

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.coroutines.launch
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.story.code.CodeBlock
import org.jetbrains.compose.storytale.gallery.ui.component.IconButton

@Composable
internal fun StoryContent(
    activeStory: Story?,
    useEmbeddedView: Boolean = false,
    modifier: Modifier = Modifier
) {
    val widthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val heightClass = currentWindowAdaptiveInfo().windowSizeClass.windowHeightSizeClass

    val isSmallHeight = heightClass == WindowHeightSizeClass.COMPACT
    val isSmallWidth = widthClass == WindowWidthSizeClass.COMPACT

    val useTabs = isSmallHeight || useEmbeddedView

    Box(modifier = modifier) {
        val showOverlayParameters = remember { mutableStateOf(false) }

        val previewContent = @Composable {
            StoryPreview(activeStory, useEmbeddedView, isSmallWidth, showOverlayParameters)
        }

        val codeContent = movableContentOf<BoxScope> { boxScope ->
            with(boxScope) {
                StoryCodeViewer(activeStory?.code ?: "")
            }
        }

        AnimatedContent(
            targetState = useTabs,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
        ) { useTabs ->
            if (useTabs) {
                StoryTabbedView(modifier, previewContent, codeContent)
            } else {
                StoryPreviewAndCodeStacked(
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
private fun StoryPreview(
    activeStory: Story? = null,
    useEmbeddedView: Boolean = false,
    isSmallWidth: Boolean = false,
    showOverlayParameters: MutableState<Boolean>
) {
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

@Composable
private fun BoxScope.StoryCodeViewer(code: String) {
    CodeBlock(code, modifier = Modifier.fillMaxSize())
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

@Composable
private fun StoryPreviewAndCodeStacked(
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
                StoryParametersList(
                    storyParameters!!,
                    modifier = Modifier.fillMaxSize().padding(8.dp).verticalScroll(rememberScrollState(0)),
                )
            }
        }
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
                StoryParametersList(
                    activeStory?.parameters ?: emptyList(),
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
private fun StoryTabbedView(
    modifier: Modifier = Modifier,
    previewContent: @Composable () -> Unit,
    codeContent: @Composable BoxScope.() -> Unit,
) {
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
}
