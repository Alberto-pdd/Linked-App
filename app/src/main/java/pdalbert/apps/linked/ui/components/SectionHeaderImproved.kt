package pdalbert.apps.linked.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pdalbert.apps.linked.ui.theme.Accent
import pdalbert.apps.linked.ui.theme.Ink
import pdalbert.apps.linked.ui.theme.Inter

@Composable
fun SectionHeaderImproved(
    title: String,
    showSeeAll: Boolean = true,
    onSeeAllClicked: () -> Unit,
    modifier: Modifier = Modifier,
    filters: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 4.dp)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Ink,
                letterSpacing = (-0.3).sp,
                lineHeight = 24.sp
            )
            filters()
        }

        if (showSeeAll) {
            Text(
                text = "Ver todos →",
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Accent,
                modifier = Modifier.clickable { onSeeAllClicked() }
            )
        }
    }
}
