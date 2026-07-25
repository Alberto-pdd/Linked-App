package pdalbert.apps.linked.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pdalbert.apps.linked.ui.animations.AnimatedListItem
import pdalbert.apps.linked.ui.animations.rememberVisibilityTracker
import java.util.UUID

@Composable
fun <T> FilteredAnimatedList(
    allItems: List<T>,
    filteredItems: List<T>,
    idExtractor: (T) -> UUID,
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 10.dp,
    enterDuration: Int = 400,
    exitDuration: Int = 300,
    staggerDelay: Int = 60,
    offsetFraction: Float = 1f / 3f,
    content: @Composable (T) -> Unit
) {
    val visibleIds = rememberVisibilityTracker(filteredItems) { idExtractor(it) }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        itemsIndexed(allItems, key = { _, item -> idExtractor(item) }) { index, item ->
            AnimatedListItem(
                isVisible = visibleIds.contains(idExtractor(item)),
                index = index,
                enterDuration = enterDuration,
                exitDuration = exitDuration,
                staggerDelay = staggerDelay,
                offsetFraction = offsetFraction,
                content = { content(item) }
            )
        }
    }
}