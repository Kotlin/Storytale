import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.storytale.runtime.Story

@Composable
fun LazyStoryList(
  storyList: List<Story>,
  modifier: Modifier = Modifier,
  state: LazyListState = rememberLazyListState(),
  contentPadding: PaddingValues = PaddingValues(0.dp),
  reverseLayout: Boolean = false,
  verticalArrangement: Arrangement.Vertical =
    if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
  horizontalAlignment: Alignment.Horizontal = Alignment.Start,
  flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
  userScrollEnabled: Boolean = true
) {
  LazyColumn(
    modifier = modifier,
    state = state,
    contentPadding = contentPadding,
    flingBehavior = flingBehavior,
    horizontalAlignment = horizontalAlignment,
    verticalArrangement = verticalArrangement,
    reverseLayout = reverseLayout,
    userScrollEnabled = userScrollEnabled
  ) {
    items(
      items = storyList,
      key = { it.storyName },
      contentType = { it }
    ) {
      it.content()
    }
  }
}
