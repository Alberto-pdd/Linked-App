package pdalbert.apps.linked.ui.screens.tags

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import pdalbert.apps.linked.data.model.Tag
import pdalbert.apps.linked.data.model.TagColor
import pdalbert.apps.linked.ui.components.ColorPicker
import pdalbert.apps.linked.ui.components.Toast
import pdalbert.apps.linked.ui.screens.home.FieldInput
import pdalbert.apps.linked.ui.theme.Accent
import pdalbert.apps.linked.ui.theme.Background
import pdalbert.apps.linked.ui.theme.Border
import pdalbert.apps.linked.ui.theme.Ink
import pdalbert.apps.linked.ui.theme.InkMuted
import pdalbert.apps.linked.ui.theme.Inter
import pdalbert.apps.linked.ui.theme.Surface
import pdalbert.apps.linked.viewmodel.TagsViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsManagementScreen(
    navController: NavHostController,
    viewModel: TagsViewModel
) {
    val tags by viewModel.tags.collectAsState()
    val showAddDialog by viewModel.showAddDialog.collectAsState()
    val editingTag by viewModel.editingTag.collectAsState()
    val deleteTagId by viewModel.deleteTagId.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            viewModel.onToastShown()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Gestionar etiquetas",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Ink
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Ink
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Background
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tags) { tag ->
                TagItem(
                    tag = tag,
                    onEdit = { viewModel.onEditClicked(tag) },
                    onDelete = { viewModel.onDeleteClicked(tag.id) }
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Surface)
                        .border(1.5.dp, Border, RoundedCornerShape(16.dp))
                        .clickable { viewModel.onAddClicked() }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Accent,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Crear nueva etiqueta",
                            fontFamily = Inter,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Accent
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        TagDialog(
            tag = editingTag,
            onDismiss = { viewModel.onDialogDismissed() },
            onSave = { viewModel.onTagSaved(it) }
        )
    }

    if (deleteTagId != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onDeleteCancelled() },
            title = {
                Text(
                    text = "Eliminar etiqueta",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que quieres eliminar esta etiqueta?",
                    fontFamily = Inter
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.onDeleteConfirmed() }) {
                    Text("Eliminar", fontFamily = Inter, color = Color(0xFFEF4444))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDeleteCancelled() }) {
                    Text("Cancelar", fontFamily = Inter)
                }
            }
        )
    }

    toastMessage?.let { message ->
        Toast(message = message, onDismiss = { viewModel.onToastShown() })
    }
}

@Composable
private fun TagItem(
    tag: Tag,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val tagColor = try {
        TagColor.valueOf(tag.colorName)
    } catch (_: Exception) {
        TagColor.DEFAULT
    }
    val fgColor = Color(android.graphics.Color.parseColor(tagColor.fgColor))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Surface)
            .border(1.5.dp, Border, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(fgColor)
            )
            Text(
                text = tag.name,
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Ink
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(onClick = onEdit) {
                Text(
                    text = "Editar",
                    fontFamily = Inter,
                    fontSize = 12.sp,
                    color = InkMuted
                )
            }
            TextButton(onClick = onDelete) {
                Text(
                    text = "Eliminar",
                    fontFamily = Inter,
                    fontSize = 12.sp,
                    color = Color(0xFFEF4444)
                )
            }
        }
    }
}

@Composable
private fun TagDialog(
    tag: Tag?,
    onDismiss: () -> Unit,
    onSave: (Tag) -> Unit
) {
    var name by remember { mutableStateOf(tag?.name ?: "") }
    var selectedColor by remember { mutableStateOf(tag?.colorName ?: TagColor.DEFAULT.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (tag != null) "Editar etiqueta" else "Nueva etiqueta",
                fontFamily = Inter,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                FieldInput(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Nombre de la etiqueta"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "COLOR",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = InkMuted,
                    modifier = Modifier.padding(bottom = 7.dp)
                )
                ColorPicker(
                    colors = TagColor.entries.map { tc ->
                        pdalbert.apps.linked.ui.components.ColorOption(
                            bg = tc.name,
                            color = Color(android.graphics.Color.parseColor(tc.bgColor))
                        )
                    },
                    selectedColor = selectedColor,
                    onColorSelected = { selectedColor = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        val newTag = Tag(
                            id = tag?.id ?: UUID.randomUUID(),
                            name = name,
                            colorName = selectedColor
                        )
                        onSave(newTag)
                    }
                }
            ) {
                Text("Guardar", fontFamily = Inter, color = Accent)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", fontFamily = Inter)
            }
        }
    )
}
