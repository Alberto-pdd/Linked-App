package pdalbert.apps.linked.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pdalbert.apps.linked.ui.components.DeleteConfirmDialog
import pdalbert.apps.linked.ui.components.FilterChips
import pdalbert.apps.linked.ui.components.LinkCard
import pdalbert.apps.linked.ui.components.SearchBar
import pdalbert.apps.linked.ui.components.TopNav
import pdalbert.apps.linked.ui.components.Toast
import pdalbert.apps.linked.ui.theme.Accent
import pdalbert.apps.linked.ui.theme.Background
import pdalbert.apps.linked.ui.theme.Danger
import pdalbert.apps.linked.ui.theme.Ink
import pdalbert.apps.linked.ui.theme.InkMuted
import pdalbert.apps.linked.ui.theme.Manrope
import pdalbert.apps.linked.ui.theme.Surface

@Composable
fun AllLinksScreen(
    navController: NavHostController,
    viewModel: AllLinksViewModel
) {
    val filteredLinks by viewModel.filteredLinks.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val activeTag by viewModel.activeTag.collectAsState()
    val availableTags by viewModel.availableTags.collectAsState()
    val showAddSheet by viewModel.showAddSheet.collectAsState()
    val editingLink by viewModel.editingLink.collectAsState()
    val deleteLinkId by viewModel.deleteLinkId.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    var swipedLinkId by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(WindowInsets.systemBars.asPaddingValues())
        ) {
            // Back button + Title + Add
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Ink,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = "Todos los links",
                        fontFamily = Manrope,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Ink,
                        letterSpacing = (-0.5).sp
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(Accent)
                        .clickable { viewModel.onAddClicked() }
                        .padding(horizontal = 16.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "+ Añadir",
                        fontFamily = Manrope,
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
                placeholder = "Buscar enlaces...",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            // Filter chips
            if (availableTags.size > 1) {
                FilterChips(
                    tags = availableTags,
                    activeTag = activeTag,
                    onTagSelected = viewModel::onTagSelected,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            // Count
            Text(
                text = "${filteredLinks.size} enlace${if (filteredLinks.size != 1) "s" else ""}",
                fontFamily = Manrope,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = InkMuted,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )

            // List
            if (filteredLinks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "\uD83D\uDD17", fontSize = 36.sp)
                        Text(
                            text = "Sin enlaces",
                            fontFamily = Manrope,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Ink,
                            letterSpacing = (-0.4).sp
                        )
                        Text(
                            text = "Añade tu primer enlace\npulsando el botón de arriba.",
                            fontFamily = Manrope,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = InkMuted,
                            lineHeight = 20.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredLinks, key = { it.id.toString() }) { link ->
                        Box {
                            // Swipe actions (behind the card)
                            if (swipedLinkId == link.id.toString()) {
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .clip(RoundedCornerShape(18.dp)),
                                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFF3B82C4))
                                            .clickable {
                                                swipedLinkId = null
                                                viewModel.onEditClicked(link)
                                            }
                                            .padding(horizontal = 18.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(3.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.Edit,
                                                contentDescription = "Editar",
                                                tint = Color.White
                                            )
                                            Text(
                                                text = "Editar",
                                                fontFamily = Manrope,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.5.sp,
                                                color = Color.White
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .background(Danger)
                                            .clickable {
                                                swipedLinkId = null
                                                viewModel.onDeleteClicked(link.id)
                                            }
                                            .padding(horizontal = 18.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(3.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.Delete,
                                                contentDescription = "Eliminar",
                                                tint = Color.White
                                            )
                                            Text(
                                                text = "Eliminar",
                                                fontFamily = Manrope,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.5.sp,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }

                            LinkCard(
                                link = link,
                                onClick = {
                                    if (swipedLinkId == link.id.toString()) {
                                        swipedLinkId = null
                                    } else if (swipedLinkId != null) {
                                        swipedLinkId = null
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // Toast
        Toast(
            message = toastMessage,
            onDismiss = viewModel::onToastShown
        )
    }

    // Add/Edit sheet
    if (showAddSheet) {
        AddLinkSheet(
            editingLink = editingLink,
            onDismiss = viewModel::onSheetDismissed,
            onSave = viewModel::onLinkSaved
        )
    }

    // Delete confirmation
    if (deleteLinkId != null) {
        DeleteConfirmDialog(
            title = "Eliminar enlace",
            message = "¿Seguro que quieres eliminarlo?\nEsta acción no se puede deshacer.",
            onConfirm = viewModel::onDeleteConfirmed,
            onDismiss = viewModel::onDeleteCancelled
        )
    }
}
