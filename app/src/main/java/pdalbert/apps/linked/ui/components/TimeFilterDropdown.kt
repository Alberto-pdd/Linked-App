package pdalbert.apps.linked.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pdalbert.apps.linked.ui.theme.Border
import pdalbert.apps.linked.ui.theme.InkDecorations
import pdalbert.apps.linked.ui.theme.Inter
import pdalbert.apps.linked.ui.theme.Surface

@Composable
fun TimeFilterDropdown(
    selectedText: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 360f else 270f,
        animationSpec = tween(durationMillis = 200),
        label = "arrow_rotation"
    )

    Row(modifier = modifier) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .background(Surface)
                .border(1.dp, Border, RoundedCornerShape(100.dp))
                .clickable { expanded = true }
                .padding(horizontal = 10.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedContent(
                targetState = selectedText,
                transitionSpec = {
                    (slideInHorizontally(tween(200)) { it } + fadeIn(tween(200)))
                        .togetherWith(slideOutHorizontally(tween(200)) { -it } + fadeOut(tween(200)))
                },
                label = "filter_text"
            ) { text ->
                Text(
                    text = text,
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = InkDecorations,
                    lineHeight = 18.sp
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = InkDecorations,
                modifier = Modifier
                    .size(14.dp)
                    .rotate(rotation)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                val isSelected = option == selectedText
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            fontFamily = Inter,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (isSelected) pdalbert.apps.linked.ui.theme.Accent else pdalbert.apps.linked.ui.theme.Ink
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    modifier = if (isSelected) {
                        Modifier.background(pdalbert.apps.linked.ui.theme.AccentBg)
                    } else {
                        Modifier
                    }
                )
            }
        }
    }
}
