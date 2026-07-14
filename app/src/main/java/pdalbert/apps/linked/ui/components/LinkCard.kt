package pdalbert.apps.linked.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pdalbert.apps.linked.data.model.Link
import pdalbert.apps.linked.ui.theme.Border
import pdalbert.apps.linked.ui.theme.Ink
import pdalbert.apps.linked.ui.theme.InkMuted
import pdalbert.apps.linked.ui.theme.Manrope
import pdalbert.apps.linked.ui.theme.Surface

@Composable
fun LinkCard(
    link: Link,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tagColors = when (link.tag.lowercase()) {
        "diseño" -> Pair(Color(0xFFFEF3C7), Color(0xFF92630A))
        "ia" -> Pair(Color(0xFFDCFCE7), Color(0xFF166534))
        "dev" -> Pair(Color(0xFFEDE9FE), Color(0xFF5B21B6))
        "video" -> Pair(Color(0xFFEDE9FE), Color(0xFF5B21B6))
        "noticias" -> Pair(Color(0xFFEBF3FB), Color(0xFF3B82C4))
        else -> Pair(Color(0xFFEBF3FB), Color(0xFF3B82C4))
    }

    val bgColorParsed = try { Color(android.graphics.Color.parseColor(link.bgColor)) }
    catch (_: Exception) { Color(0xFFEBF3FB) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Surface)
            .border(1.5.dp, Border, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(13.dp, 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(bgColorParsed),
            contentAlignment = Alignment.Center
        ) {
            Text(text = link.emoji, fontSize = 18.sp)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = link.title,
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp,
                color = Ink,
                letterSpacing = (-0.2).sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = link.url,
                fontFamily = Manrope,
                fontWeight = FontWeight.Normal,
                fontSize = 11.5.sp,
                color = InkMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        if (link.tag.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(tagColors.first)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = link.tag,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.5.sp,
                    color = tagColors.second
                )
            }
        }
    }
}
