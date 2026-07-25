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
    ColorOption("#FFCDD2", Color(0xFFFFCDD2)),
    ColorOption("#F8BBD0", Color(0xFFF8BBD0)),
    ColorOption("#E1BEE7", Color(0xFFE1BEE7)),
    ColorOption("#D1C4E9", Color(0xFFD1C4E9)),
    ColorOption("#C5CAE9", Color(0xFFC5CAE9)),
    ColorOption("#BBDEFB", Color(0xFFBBDEFB)),
    ColorOption("#B3E5FC", Color(0xFFB3E5FC)),
    ColorOption("#B2EBF2", Color(0xFFB2EBF2)),
    ColorOption("#B2DFDB", Color(0xFFB2DFDB)),
    ColorOption("#C8E6C9", Color(0xFFC8E6C9)),
    ColorOption("#DCEDC8", Color(0xFFDCEDC8)),
    ColorOption("#FFF9C4", Color(0xFFFFFF9C4)),
    ColorOption("#FFE0B2", Color(0xFFFFE0B2)),
    ColorOption("#FFCCBC", Color(0xFFFFCCBC)),
    ColorOption("#D7CCC8", Color(0xFFD7CCC8)),
    ColorOption("#CFD8DC", Color(0xFFCFD8DC))
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
