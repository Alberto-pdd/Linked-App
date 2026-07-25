package pdalbert.apps.linked.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pdalbert.apps.linked.ui.theme.Border
import pdalbert.apps.linked.ui.theme.InkMuted
import pdalbert.apps.linked.ui.theme.Surface

@Composable
fun BackArrowContainer(
    modifier: Modifier = Modifier,
    containerSize: Int = 36,
    iconSize: Int = 23,
    containerColor: Color = Surface,
    iconColor: Color = InkMuted,
    borderColor: Color = Border,
    onClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .size(containerSize.dp)
            .clip(shape)
            .background(containerColor)
            .border(1.5.dp, borderColor, shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .size(iconSize.dp)
                .offset(x = -0.5.dp)
        )
    }
}
