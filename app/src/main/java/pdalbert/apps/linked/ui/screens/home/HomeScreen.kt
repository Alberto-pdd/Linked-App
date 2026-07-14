package pdalbert.apps.linked.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pdalbert.apps.linked.R
import pdalbert.apps.linked.ui.components.FolderCard
import pdalbert.apps.linked.ui.components.LinkCard
import pdalbert.apps.linked.ui.components.SearchBar
import pdalbert.apps.linked.ui.theme.Accent
import pdalbert.apps.linked.ui.theme.Background
import pdalbert.apps.linked.ui.theme.Border
import pdalbert.apps.linked.ui.theme.Ink
import pdalbert.apps.linked.ui.theme.InkDecorations
import pdalbert.apps.linked.ui.theme.InkMuted
import pdalbert.apps.linked.ui.theme.Manrope
import pdalbert.apps.linked.ui.theme.Surface
import pdalbert.apps.linked.ui.theme.White

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    val links by viewModel.links.collectAsState()
    val folders by viewModel.folders.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredLinks by viewModel.filteredLinks.collectAsState()
    val filteredFolders by viewModel.filteredFolders.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()

    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { destination ->
            when (destination) {
                "settings" -> navController.navigate("settings")
                "all_links" -> navController.navigate("all_links")
                "all_folders" -> navController.navigate("all_folders")
            }
            viewModel.onNavigationHandled()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.linked_logo),
                    contentDescription = "Linked logo",
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Linked",
                    fontFamily = Manrope,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = Ink,
                    letterSpacing = (-0.5).sp
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Surface)
                    .border(1.dp, Border, CircleShape)
                    .clickable { viewModel.onAvatarClicked() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.ifEmpty { "?" },
                    fontFamily = Manrope,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = InkMuted
                )
            }
        }

        // Search
        SearchBar(
            query = searchQuery,
            onQueryChanged = viewModel::onSearchQueryChanged,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        // Sección Links
        SectionHeader(
            count = "Links",
            onArrowClick = viewModel::onSeeAllLinksClicked,
            numberInList = links.size
        )

        if (filteredLinks.isEmpty()) {
            EmptyState(
                icon = "\uD83D\uDD17",
                title = "Sin enlaces",
                subtitle = "Añade tu primer enlace\ndesde \"Todos los links\"",
                modifier = Modifier.weight(2f)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(2f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredLinks, key = { it.id.toString() }) { link ->
                    LinkCard(
                        link = link,
                        onClick = { /* Fase 10: link detail */ }
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
            color = Border,
            thickness = 1.dp
        )

        // Sección Carpetas
        SectionHeader(
            count = "Carpetas",
            onArrowClick = viewModel::onSeeAllFoldersClicked,
            numberInList = folders.size
        )

        if (filteredFolders.isEmpty()) {
            EmptyState(
                icon = "\uD83D\uDCC2",
                title = "Sin carpetas",
                subtitle = "Crea tu primera carpeta\ndesde \"Carpetas\"",
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredFolders, key = { it.id.toString() }) { folder ->
                    FolderCard(
                        folder = folder,
                        linkCount = links.count { it.folderIds.contains(folder.id) },
                        createdDate = "—",
                        onClick = { /* Navegar a carpeta */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    count: String,
    onArrowClick: () -> Unit,
    modifier: Modifier = Modifier,
    numberInList: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        //horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = count,
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 21.sp,
            color = InkMuted
        )

        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(start = 10.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(White)
                .border(1.5.dp, Border, RoundedCornerShape(12.dp))
                .clickable { onArrowClick() }
                .padding(horizontal = 8.dp, vertical = 4.5.dp)
                .align(Alignment.CenterVertically)

        ) {
            Row (
//                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = numberInList.toString(),
                    fontFamily = Manrope,
                    color = InkDecorations,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 5.dp)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Ver todos",
                    tint = InkDecorations,
                    modifier = Modifier
                        .size(18.dp)
                )
            }

            Box (

            ) {

            }
        }
    }
}

@Composable
private fun EmptyState(
    icon: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = icon, fontSize = 32.sp)
            Text(
                text = title,
                fontFamily = Manrope,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = Ink,
                letterSpacing = (-0.3).sp
            )
            Text(
                text = subtitle,
                fontFamily = Manrope,
                fontWeight = FontWeight.Normal,
                fontSize = 12.5.sp,
                color = InkMuted,
                lineHeight = 18.sp
            )
        }
    }
}
