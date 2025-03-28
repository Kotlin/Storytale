package org.jetbrains.compose.storytale.gallery.story

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.ui.component.CenterRow
import org.jetbrains.compose.storytale.gallery.ui.component.Gap
import org.jetbrains.compose.storytale.gallery.ui.component.NumberChip

@Composable
fun StoryList(
    activeStoryId: Int,
    stories: List<Story>,
    onSelectStory: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) = LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(bottom = 20.dp),
) {
    item {
        CenterRow(Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Stories",
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
            )
            NumberChip(stories.size)
        }
        Gap(14.dp)
        Column(
            verticalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            val groupedStories = remember { stories.groupBy { it.group } }

            groupedStories.forEach { (group, stories) ->
                Text(
                    text = group,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 5.dp),
                )

                Column(modifier = Modifier.padding(start = 10.dp)) {
                    stories.forEach { item ->
                        StoryListItem(
                            story = item,
                            selected = item.id == activeStoryId,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                            onClick = { onSelectStory(item.id) },
                        )
                    }
                }
            }
        }
    }
}
