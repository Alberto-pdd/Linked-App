package pdalbert.apps.linked.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pdalbert.apps.linked.R
import pdalbert.apps.linked.ui.animations.rememberStaggerState
import pdalbert.apps.linked.data.model.Folder
import pdalbert.apps.linked.data.model.Link
import pdalbert.apps.linked.ui.components.FolderCard
import pdalbert.apps.linked.ui.components.HomeFAB
import pdalbert.apps.linked.ui.components.LinkCard
import pdalbert.apps.linked.ui.components.SearchBar
import pdalbert.apps.linked.ui.components.SectionHeaderImproved
import pdalbert.apps.linked.ui.components.SortDirectionButton
import pdalbert.apps.linked.ui.components.TagFilterChips
import pdalbert.apps.linked.ui.components.TimeFilterDropdown
import pdalbert.apps.linked.ui.components.Toast
import pdalbert.apps.linked.ui.theme.Accent
import pdalbert.apps.linked.ui.theme.Background
import pdalbert.apps.linked.ui.theme.Ink
import pdalbert.apps.linked.ui.theme.InkMuted
import pdalbert.apps.linked.ui.theme.Inter
import pdalbert.apps.linked.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    val folders by viewModel.folders.collectAsState()
    val tags by viewModel.tags.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val activeTags by viewModel.activeTags.collectAsState()
    val linkSortAscending by viewModel.linkSortAscending.collectAsState()
    val linkTimePeriod by viewModel.linkTimePeriod.collectAsState()
    val folderSortOption by viewModel.folderSortOption.collectAsState()
    val filteredLinks by viewModel.filteredLinks.collectAsState()
    val filteredFolders by viewModel.filteredFolders.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()
    val showAddLinkSheet by viewModel.showAddLinkSheet.collectAsState()
    val showAddFolderSheet by viewModel.showAddFolderSheet.collectAsState()
    val editingLink by viewModel.editingLink.collectAsState()
    val editingFolder by viewModel.editingFolder.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

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

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            viewModel.onToastShown()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.linked_logo),
                        contentDescription = "Linked logo",
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Text(
                        text = "Linked",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Ink,
                        letterSpacing = (-0.4).sp,
                        lineHeight = 32.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Accent)
                        .clickable { viewModel.onAvatarClicked() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.ifEmpty { "?" },
                        fontFamily = Inter,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }

            // Search
            SearchBar(
                query = searchQuery,
                onQueryChanged = viewModel::onSearchQueryChanged,
                onClear = { viewModel.onSearchQueryChanged("") },
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
            )

            // Tag filters
            if (tags.isEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
                        .background(Color.Red),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Aún no hay tags iniciados:",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = InkMuted
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(Accent)
                            .clickable { navController.navigate("tags") }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = "+ Crear tag",
                            fontFamily = Inter,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.5.sp,
                            color = Color.White
                        )
                    }
                }
            } else {
                TagFilterChips(
                    tags = tags,
                    activeTags = activeTags,
                    onTagSelected = viewModel::onTagSelected,
                    onManageTags = { navController.navigate("tags") },
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
                )
            }

            // Links + Folders sections with weight distribution (35/65)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            ) {
                // ====== LINKS SECTION: 35% ======
                Column(
                    modifier = Modifier
                        .weight(0.475f, fill = true)
                        .padding(horizontal = 20.dp)
                ) {
                    // Links section header
                    SectionHeaderImproved(
                        title = "Links recientes",
                        showSeeAll = true,
                        onSeeAllClicked = viewModel::onSeeAllLinksClicked,
                        filters = {
                            TimeFilterDropdown(
                                selectedText = linkTimePeriod,
                                options = listOf("Hoy", "Esta semana", "Este mes"),
                                onOptionSelected = viewModel::onLinkTimePeriodChanged
                            )
                            SortDirectionButton(
                                ascending = linkSortAscending,
                                onToggle = viewModel::onLinkSortDirectionChanged
                            )
                        }
                    )

                    // Links content
                    val listState = rememberLazyListState()

                    if (filteredLinks.isEmpty()) {
                        EmptyState(
                            icon = "\uD83D\uDD0F",
                            title = "No se encontraron links",
                            subtitle = "Prueba con otros filtros o etiquetas",
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    } else {
                        val linkProgress = rememberStaggerState(filteredLinks, { it.id })

                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            itemsIndexed(filteredLinks, key = { _, item: Link -> "${item.id}_${activeTags.hashCode()}_${searchQuery.hashCode()}" }) { index, link ->
                                val progress = linkProgress[link.id] ?: 1f
                                val linkTags by viewModel.getTagsForLink(link.id).collectAsState(emptyList())
                                LinkCard(
                                    link = link,
                                    tags = linkTags,
                                    timeAgo = viewModel.getTimeAgo(link.createdAt),
                                    onClick = { },
                                    onMoreOptions = { },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .graphicsLayer {
                                            val enterProgress = progress
                                            val enterTranslationY = (1f - enterProgress) * 50f
                                            val enterScale = 0.9f + (enterProgress * 0.1f)

                                            val itemKey = "${link.id}_${activeTags.hashCode()}_${searchQuery.hashCode()}"
                                            val itemInfo = listState.layoutInfo.visibleItemsInfo
                                                .firstOrNull { it.key == itemKey }
                                            val viewportHeight = listState.layoutInfo.viewportEndOffset -
                                                    listState.layoutInfo.viewportStartOffset
                                            val scrollFactor = itemInfo?.let { info ->
                                                val itemCenter = (info.offset + info.size / 2).toFloat()
                                                val viewportStart = listState.layoutInfo.viewportStartOffset.toFloat()
                                                val threshold = viewportStart + viewportHeight * 0.75f
                                                if (itemCenter <= threshold) 1f
                                                else {
                                                    val fadeZone = viewportHeight * 0.25f
                                                    1f - (itemCenter - threshold).coerceIn(0f, fadeZone) / fadeZone
                                                }
                                            } ?: 0f
                                            val scrollAlpha = 0.5f + (scrollFactor * 0.5f)
                                            val scrollScale = 0.9f + (scrollFactor * 0.1f)

                                            alpha = enterProgress * scrollAlpha
                                            translationY = enterTranslationY
                                            scaleX = enterScale * scrollScale
                                            scaleY = enterScale * scrollScale
                                        }
                                )
                            }
                        }
                    }
                }

                // ====== FOLDERS SECTION: 65% ======
                Column(
                    modifier = Modifier
                        .weight(0.525f, fill = true)
                        .shadow(16.dp, shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                        .background(Background)
                        .padding(top = 24.dp, start = 20.dp, end = 20.dp)
                ) {
                    // Folders section header
                    SectionHeaderImproved(
                        title = "Carpetas",
                        showSeeAll = true,
                        onSeeAllClicked = viewModel::onSeeAllFoldersClicked,
                        filters = {
                            TimeFilterDropdown(
                                selectedText = folderSortOption,
                                options = listOf("Más nuevo", "Más antiguo", "Más enlaces", "Menos enlaces"),
                                onOptionSelected = viewModel::onFolderSortChanged
                            )
                        }
                    )

                    // Folders content
                    if (filteredFolders.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sin carpetas",
                                fontFamily = Inter,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = InkMuted
                            )
                        }
                    } else {
                        val folderProgress = rememberStaggerState(filteredFolders, { it.id })

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            itemsIndexed(filteredFolders, key = { _, item: Folder -> "${item.id}_${activeTags.hashCode()}_${searchQuery.hashCode()}_${folderSortOption.hashCode()}" }) { index, folder ->
                                val progress = folderProgress[folder.id] ?: 1f
                                FolderCard(
                                    folder = folder,
                                    linkCount = 0,
                                    createdAtText = "",
                                    onClick = { },
                                    onMoreOptions = { },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .graphicsLayer {
                                            val enterProgress = progress
                                            val enterTranslationY = (1f - enterProgress) * 50f
                                            val enterScale = 0.9f + (enterProgress * 0.1f)

                                            alpha = enterProgress
                                            translationY = enterTranslationY
                                            scaleX = enterScale
                                            scaleY = enterScale
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }

        // FAB
        HomeFAB(
            onClick = { viewModel.onAddLinkClicked() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 36.dp)
        )
    }

    // Sheets
    if (showAddLinkSheet) {
        AddLinkSheet(
            editingLink = editingLink,
            onDismiss = { viewModel.onSheetDismissed() },
            onSave = { viewModel.onLinkSaved(it) }
        )
    }

    if (showAddFolderSheet) {
        AddFolderSheet(
            editingFolder = editingFolder,
            onDismiss = { viewModel.onSheetDismissed() },
            onSave = { viewModel.onFolderSaved(it) }
        )
    }

    toastMessage?.let { message ->
        Toast(message = message, onDismiss = { viewModel.onToastShown() })
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = icon, fontSize = 36.sp)
            Text(
                text = title,
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Ink
            )
            Text(
                text = subtitle,
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = InkMuted
            )
        }
    }
}