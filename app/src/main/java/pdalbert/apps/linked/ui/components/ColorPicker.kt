package pdalbert.apps.linked.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pdalbert.apps.linked.ui.theme.Ink

data class ColorOption(
    val bg: String,
    val color: Color
)

val defaultColorOptions = listOf(
    ColorOption("#FEF3C7", Color(0xFFFEF3C7)),
    ColorOption("#DCFCE7", Color(0xFFDCFCE7)),
    ColorOption("#EBF3FB", Color(0xFFEBF3FB)),
    ColorOption("#EDE9FE", Color(0xFFEDE9FE)),
    ColorOption("#FFE4E6", Color(0xFFD4E6FF)),
    ColorOption("#F0FDF4", Color(0xFFF0FDF4))
)

@Composable
fun ColorPicker(
    colors: List<ColorOption> = defaultColorOptions,
    selectedColor: String,
    onColorSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        colors.forEach { option ->
            val isSelected = option.bg == selectedColor
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(option.color)
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = if (isSelected) Ink else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable { onColorSelected(option.bg) }
            )
        }
    }
}
