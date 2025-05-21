package org.jetbrains.compose.storytale.gallery.material3

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.jetbrains.compose.storytale.storiesStorage

@Composable
fun EmbeddedStoryView(
    appState: StorytaleGalleryAppState,
    navHostController: NavHostController,
) {
    NavHost(
        navController = navHostController,
        startDestination = StoryScreen(""),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable<StoryScreen> {
            val args = it.toRoute<StoryScreen>()
            val storyName = args.storyName
            Column(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = storyName,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(12.dp),
                        )
                    },
                    actions = {
                        ThemeSwitcherIconButton(appState)

                        val uriHandler = LocalUriHandler.current

                        IconButton(onClick = {
                            openFullScreenStory(args, uriHandler)
                        }) {
                            Icon(imageVector = OpenInFull, contentDescription = null)
                        }
                    },
                )
                StoryContent(
                    activeStory = storiesStorage.firstOrNull {
                        it.name == storyName
                    },
                    useEmbeddedView = true,
                    modifier = Modifier.weight(1f),
                )
                HorizontalDivider()
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 12.dp)) {
                    val uriHandler = LocalUriHandler.current
                    Text(
                        text = buildAnnotatedString {
                            append("Powered by ")
                            withStyle(
                                style = SpanStyle(
                                    textDecoration = TextDecoration.Underline,
                                    color = MaterialTheme.colorScheme.primary,
                                ),
                            ) {
                                append("Storytale")
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterEnd).clickable(onClick = {
                            uriHandler.openUri("https://github.com/Kotlin/Storytale")
                        }),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

/**
 * EXPERIMENTAL!
 * A lambda to be provided by the Storytale gallery consumers to open a full screen gallery.
 */
var openFullScreenStory: (StoryScreen, UriHandler) -> Unit = { story, uriHandler ->
    println("openFullScreenStory($story) is not implemented. It should be provided by the Gallery consumer.")
}
