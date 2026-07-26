package pdalbert.apps.linked.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pdalbert.apps.linked.data.model.Folder
import pdalbert.apps.linked.ui.theme.Border
import pdalbert.apps.linked.ui.theme.Ink
import pdalbert.apps.linked.ui.theme.InkDecorations
import pdalbert.apps.linked.ui.theme.InkMuted
import pdalbert.apps.linked.ui.theme.Inter
import pdalbert.apps.linked.ui.theme.Surface

@Composable
fun FolderCard(
    folder: Folder,
    linkCount: Int,
    createdAtText: String = "",
    isFavorite: Boolean = false,
    onClick: () -> Unit,
    onMoreOptions: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val bgColorParsed = try {
        Color(android.graphics.Color.parseColor(folder.bgColor))
    } catch (_: Exception) {
        Color(0xFFFEF3C7)
    }

    Row(
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Surface)
            .border(1.5.dp, Border, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp, 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Emoji box
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(bgColorParsed),
            contentAlignment = Alignment.Center
        ) {
            Text(text = folder.emoji, fontSize = 20.sp)
        }

        // Body: name + meta
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = folder.name,
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Ink,
                letterSpacing = (-0.3).sp,
                lineHeight = 21.sp
            )
            val metaText = buildString {
                append("$linkCount enlaces")
                if (createdAtText.isNotEmpty()) {
                    append(" · Creada $createdAtText")
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Text(
                    text = metaText,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = InkMuted,
                    lineHeight = 16.sp
                )
                if (isFavorite) {
                    Text(
                        text = " · ",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = InkMuted
                    )
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Favorito",
                        tint = Color(0xFFF5A623),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        // More button (⋯)
        if (onMoreOptions != null) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable { onMoreOptions() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⋯",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = InkDecorations
                )
            }
        }
    }
}
