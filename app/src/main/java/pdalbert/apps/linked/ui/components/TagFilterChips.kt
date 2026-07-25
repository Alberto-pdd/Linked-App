package pdalbert.apps.linked.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pdalbert.apps.linked.data.model.Tag
import pdalbert.apps.linked.data.model.resolveTagColor
import pdalbert.apps.linked.data.model.toBgColor
import pdalbert.apps.linked.data.model.toBorderColor
import pdalbert.apps.linked.data.model.toFgColor
import pdalbert.apps.linked.ui.theme.Accent
import pdalbert.apps.linked.ui.theme.InkDecorations
import pdalbert.apps.linked.ui.theme.Inter

@Composable
fun TagFilterChips(
    tags: List<Tag>,
    activeTags: List<String>,
    onTagSelected: (String) -> Unit,
    onManageTags: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(modifier = Modifier.rotate(180f)) {
            BackArrowContainer(onClick = onManageTags)
        }
        Box {
            Text(
                text = "|",
                fontFamily = Inter,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = InkDecorations
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(tags) { tag ->
                val isActive = tag.name in activeTags
                val tagColor = resolveTagColor(tag.colorName)
                val bgColor: Color
                val fgColor: Color
                val borderColor: Color

                if (isActive) {
                    bgColor = Accent
                    fgColor = Color.White
                    borderColor = Accent
                } else {
                    bgColor = tagColor.toBgColor()
                    fgColor = tagColor.toFgColor()
                    borderColor = tagColor.toBorderColor()
                }

                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()
                val scale by animateFloatAsState(
                    targetValue = if (isPressed) 0.95f else 1f,
                    animationSpec = spring(dampingRatio = 0.4f, stiffness = 300f)
                )
                val animatedBg by animateColorAsState(
                    targetValue = bgColor,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
                val animatedFg by animateColorAsState(
                    targetValue = fgColor,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
                val animatedBorder by animateColorAsState(
                    targetValue = borderColor,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clip(RoundedCornerShape(100.dp))
                        .background(animatedBg)
                        .border(1.5.dp, animatedBorder, RoundedCornerShape(100.dp))
                        .clickable(interactionSource = interactionSource, indication = null) { onTagSelected(tag.name) }
                        .padding(horizontal = 16.dp, vertical = 9.dp)
                ) {
                    Text(
                        text = tag.name,
                        fontFamily = Inter,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.5.sp,
                        color = animatedFg
                    )
                }
            }
        }
    }
}
