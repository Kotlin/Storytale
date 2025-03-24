package org.jetbrains.compose.storytale.gallery.material3

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
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.Story
import org.jetbrains.compose.storytale.gallery.ui.component.Gap

@Composable
fun StoryList(
    activeStory: Story?,
    storyListItems: List<StoryListItemType>,
    expandedGroups: SnapshotStateSet<StoryListItemType.Group>,
    onItemClick: (StoryListItemType) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxHeight(),
        contentPadding = PaddingValues(8.dp),
    ) {
        items(storyListItems.size) { index ->
            val listItem = storyListItems[index]
            if (listItem is StoryListItemType.Group) {
                GroupContent(
                    group = listItem,
                    expandedGroups = expandedGroups,
                    onItemClick = onItemClick,
                    activeStory = activeStory,
                    isHighlighted = listItem.contains(activeStory),
                    modifier = Modifier.animateItem(),
                )
            } else if (listItem is StoryListItemType.StoryItem) {
                StoryListItemView(
                    isActiveStory = listItem.story == activeStory,
                    storyListItemType = listItem,
                    onClick = { onItemClick(listItem) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

private val itemShape = RoundedCornerShape(24.dp)
private val indentationDp = 12.dp

@Composable
private fun GroupContent(
    group: StoryListItemType.Group,
    expandedGroups: SnapshotStateSet<StoryListItemType.Group>,
    isHighlighted: Boolean,
    onItemClick: (StoryListItemType) -> Unit,
    activeStory: Story?,
    modifier: Modifier = Modifier,
    groupDepth: Int = 0,
) {
    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        StoryGroupLabelView(
            groupDepth = groupDepth,
            group = group,
            isExpanded = expandedGroups.contains(group),
            isHighlighted = isHighlighted,
            onClick = { onItemClick(group) },
        )

        val isExpanded = expandedGroups.contains(group)

        AnimatedVisibility(isExpanded, enter = expandVertically(), exit = shrinkVertically()) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                group.children.forEach { item ->
                    when (item) {
                        is StoryListItemType.Group -> {
                            GroupContent(
                                groupDepth = groupDepth + 1,
                                group = item,
                                expandedGroups = expandedGroups,
                                onItemClick = onItemClick,
                                activeStory = activeStory,
                                isHighlighted = item.contains(activeStory),
                            )
                        }
                        is StoryListItemType.StoryItem -> {
                            val indentation = indentationDp * (groupDepth + 1)
                            StoryListItemView(
                                isActiveStory = item.story == activeStory,
                                onClick = { onItemClick(item) },
                                storyListItemType = item,
                                modifier = Modifier.padding(start = indentation),
                            )
                        }
                    }
                }
            }
        }
    }

    Gap(4.dp)
}

@Composable
private fun StoryGroupLabelView(
    groupDepth: Int,
    group: StoryListItemType.Group,
    isExpanded: Boolean,
    isHighlighted: Boolean,
    onClick: () -> Unit,
) {
    val bgAlpha = animateFloatAsState(
        if (isHighlighted) 0.1f else 0f,
        tween(),
    ).value
    val bgColor = MaterialTheme.colorScheme.onSurface.copy(alpha = bgAlpha)
    val indentation = indentationDp * groupDepth

    Row(
        modifier = Modifier
            .padding(start = indentation)
            .fillMaxWidth()
            .clip(itemShape)
            .background(
                color = bgColor,
                shape = itemShape,
            ).clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = group.groupLabel,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )

        val rotation by animateFloatAsState(
            targetValue = if (isExpanded) 180f else 0f,
        )

        Icon(
            imageVector = Icons.Outlined.ArrowDropDown,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically).padding(end = 16.dp)
                .rotate(rotation),
        )
    }
}

@Composable
private fun StoryListItemView(
    isActiveStory: Boolean,
    storyListItemType: StoryListItemType.StoryItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgAlpha = animateFloatAsState(
        if (isActiveStory) 1f else 0f,
        tween(),
    ).value

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(itemShape)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = bgAlpha),
                shape = itemShape,
            ).clickable { onClick() },
    ) {
        Text(
            text = storyListItemType.story.name,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = if (isActiveStory) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )
    }
}

sealed interface StoryListItemType {

    class Group(
        val groupLabel: String,
        val children: List<StoryListItemType>,
    ) : StoryListItemType {

        override fun hashCode(): Int {
            return groupLabel.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Group

            if (groupLabel != other.groupLabel) return false
            if (children != other.children) return false

            return true
        }

        fun contains(story: Story?): Boolean {
            if (story == null) return false
            return children.any { child ->
                when (child) {
                    is StoryItem -> child.story === story
                    is Group -> child.contains(story)
                }
            }
        }
    }

    class StoryItem(val story: Story) : StoryListItemType {
        override fun hashCode(): Int {
            return story.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false
            other as StoryItem
            return story == other.story
        }
    }
}
