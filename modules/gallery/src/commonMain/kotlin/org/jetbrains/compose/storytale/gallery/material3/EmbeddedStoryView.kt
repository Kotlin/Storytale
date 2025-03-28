package org.jetbrains.compose.storytale.gallery.material3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.Story

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
