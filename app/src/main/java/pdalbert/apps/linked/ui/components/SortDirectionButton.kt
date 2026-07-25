package pdalbert.apps.linked.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import pdalbert.apps.linked.ui.theme.Border
import pdalbert.apps.linked.ui.theme.InkDecorations
import pdalbert.apps.linked.ui.theme.Surface

@Composable
fun SortDirectionButton(
    ascending: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (ascending) 0f else 180f,
        animationSpec = tween(200),
        label = "sort_arrow"
    )

    Box(
        modifier = modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(Surface)
            .border(1.dp, Border, CircleShape)
            .clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = null,
            tint = InkDecorations,
            modifier = Modifier
                .size(14.dp)
                .rotate(rotation)
        )
    }
}
