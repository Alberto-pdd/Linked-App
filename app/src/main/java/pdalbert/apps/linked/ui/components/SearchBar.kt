package pdalbert.apps.linked.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pdalbert.apps.linked.R
import pdalbert.apps.linked.ui.theme.Border
import pdalbert.apps.linked.ui.theme.Ink
import pdalbert.apps.linked.ui.theme.InkDecorations
import pdalbert.apps.linked.ui.theme.Inter
import pdalbert.apps.linked.ui.theme.Surface

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClear: () -> Unit,
    placeholder: String = "Buscar en tus links y carpetas…",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Surface)
            .border(1.5.dp, Border, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.search_big_variant),
                contentDescription = "Buscar",
                tint = InkDecorations,
                modifier = Modifier.size(22.dp)
            )

            Box(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = InkDecorations
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChanged,
                    textStyle = TextStyle(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = Ink
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (query.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .clickable { onClear() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = InkDecorations
                    )
                }
            }
        }
    }
}
