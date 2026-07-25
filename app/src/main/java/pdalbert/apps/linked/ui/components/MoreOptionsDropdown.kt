package pdalbert.apps.linked.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pdalbert.apps.linked.ui.theme.InkDecorations
import pdalbert.apps.linked.ui.theme.Inter

@Composable
fun MoreOptionsDropdown(
    onEdit: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onCopyUrl: (() -> Unit)? = null,
    onShare: (() -> Unit)? = null,
    onOpen: (() -> Unit)? = null,
    isFavorite: Boolean = false,
    onToggleFavorite: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Text(
            text = "⋯",
            fontFamily = Inter,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = InkDecorations,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.padding(0.dp)
        ) {
            onEdit?.let {
                DropdownMenuItem(
                    text = { Text("Editar", fontFamily = Inter, fontSize = 13.sp) },
                    onClick = {
                        it()
                        expanded = false
                    }
                )
            }
            onDelete?.let {
                DropdownMenuItem(
                    text = { Text("Eliminar", fontFamily = Inter, fontSize = 13.sp) },
                    onClick = {
                        it()
                        expanded = false
                    }
                )
            }
            onCopyUrl?.let {
                DropdownMenuItem(
                    text = { Text("Copiar URL", fontFamily = Inter, fontSize = 13.sp) },
                    onClick = {
                        it()
                        expanded = false
                    }
                )
            }
            onShare?.let {
                DropdownMenuItem(
                    text = { Text("Compartir", fontFamily = Inter, fontSize = 13.sp) },
                    onClick = {
                        it()
                        expanded = false
                    }
                )
            }
            onOpen?.let {
                DropdownMenuItem(
                    text = { Text("Abrir", fontFamily = Inter, fontSize = 13.sp) },
                    onClick = {
                        it()
                        expanded = false
                    }
                )
            }
            onToggleFavorite?.let {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = if (isFavorite) "Quitar de favoritos" else "Marcar como favorito",
                            fontFamily = Inter,
                            fontSize = 13.sp
                        )
                    },
                    onClick = {
                        it()
                        expanded = false
                    }
                )
            }
        }
    }
}
