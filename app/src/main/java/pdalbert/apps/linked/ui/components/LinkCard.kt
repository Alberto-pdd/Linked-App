package pdalbert.apps.linked.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pdalbert.apps.linked.data.model.Link
import pdalbert.apps.linked.data.model.Tag
import pdalbert.apps.linked.data.model.TagColor
import pdalbert.apps.linked.ui.theme.Border
import pdalbert.apps.linked.ui.theme.Ink
import pdalbert.apps.linked.ui.theme.InkDecorations
import pdalbert.apps.linked.ui.theme.Inter
import pdalbert.apps.linked.ui.theme.Surface

@Composable
fun LinkCard(
    link: Link,
    tags: List<Tag> = emptyList(),
    timeAgo: String = "",
    onClick: () -> Unit,
    onMoreOptions: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val bgColorParsed = try {
        Color(android.graphics.Color.parseColor(link.bgColor))
    } catch (_: Exception) {
        Color(0xFFEBF3FB)
    }

    val domain = try {
        Uri.parse(link.url).host?.removePrefix("www.") ?: ""
    } catch (_: Exception) {
        ""
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Surface)
            .border(1.5.dp, Border, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(12.dp, 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Emoji box
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(bgColorParsed),
            contentAlignment = Alignment.Center
        ) {
            Text(text = link.emoji, fontSize = 18.sp)
        }

        // Body: title + time
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = link.title,
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Ink,
                letterSpacing = (-0.2).sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val subtitle = buildString {
                if (domain.isNotEmpty()) append(domain)
                if (domain.isNotEmpty() && timeAgo.isNotEmpty()) append(" · ")
                if (timeAgo.isNotEmpty()) append(timeAgo)
            }
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = InkDecorations,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Right: tag pill + more button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Tag pill (first tag + remaining count indicator)
            val firstTag = tags.firstOrNull()
            val remainingCount = tags.size - 1
            if (firstTag != null) {
                val tagColor = try {
                    TagColor.valueOf(firstTag.colorName)
                } catch (_: Exception) {
                    TagColor.DEFAULT
                }
                val bgColor = Color(android.graphics.Color.parseColor(tagColor.bgColor))
                val fgColor = Color(android.graphics.Color.parseColor(tagColor.fgColor))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(bgColor)
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = firstTag.name,
                            fontFamily = Inter,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = fgColor,
                            lineHeight = 16.sp
                        )
                    }
                    if (remainingCount > 0) {
                        Text(
                            text = "+$remainingCount",
                            fontFamily = Inter,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            color = InkDecorations
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
}
