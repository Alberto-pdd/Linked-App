package pdalbert.apps.linked.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import pdalbert.apps.linked.data.model.Link
import pdalbert.apps.linked.ui.components.ColorPicker
import pdalbert.apps.linked.ui.components.EmojiPicker
import pdalbert.apps.linked.ui.components.defaultColorOptions
import pdalbert.apps.linked.ui.theme.Accent
import pdalbert.apps.linked.ui.theme.Background
import pdalbert.apps.linked.ui.theme.Border
import pdalbert.apps.linked.ui.theme.Ink
import pdalbert.apps.linked.ui.theme.InkMuted
import pdalbert.apps.linked.ui.theme.Manrope
import pdalbert.apps.linked.ui.theme.Surface
import java.util.UUID

private val linkEmojis = listOf(
    "\uD83D\uDD17", "\uD83C\uDFA8", "\uD83E\uDD16", "\uD83D\uDCF0", "\uD83C\uDFAC",
    "\uD83D\uDEE0\uFE0F", "\uD83D\uDCCA", "\uD83C\uDF10", "\uD83D\uDCD6", "\u2B50",
    "\uD83D\uDCA1", "\uD83D\uDE80", "\uD83C\uDFAF", "\uD83D\uDCF1", "\uD83D\uDD16"
)

private val defaultBgColors = defaultColorOptions.map { it.bg }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLinkSheet(
    editingLink: Link? = null,
    onDismiss: () -> Unit,
    onSave: (Link) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedEmoji by remember { mutableStateOf(editingLink?.emoji ?: "\uD83D\uDD17") }
    var selectedColor by remember { mutableStateOf(editingLink?.bgColor ?: "#EBF3FB") }
    var url by remember { mutableStateOf(editingLink?.url ?: "") }
    var title by remember { mutableStateOf(editingLink?.title ?: "") }
    var tag by remember { mutableStateOf(editingLink?.tag ?: "") }

    var urlError by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Border)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 44.dp)
        ) {
            Text(
                text = if (editingLink != null) "Editar enlace" else "Nuevo enlace",
                fontFamily = Manrope,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Ink,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Text(
                text = "ICONO",
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = InkMuted,
                letterSpacing = 0.4.sp,
                modifier = Modifier.padding(bottom = 7.dp)
            )
            EmojiPicker(
                emojis = linkEmojis,
                selectedEmoji = selectedEmoji,
                onEmojiSelected = { selectedEmoji = it }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "COLOR",
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = InkMuted,
                letterSpacing = 0.4.sp,
                modifier = Modifier.padding(bottom = 7.dp)
            )
            ColorPicker(
                selectedColor = selectedColor,
                onColorSelected = { selectedColor = it }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "URL",
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = InkMuted,
                letterSpacing = 0.4.sp,
                modifier = Modifier.padding(bottom = 7.dp)
            )
            FieldInput(
                value = url,
                onValueChange = {
                    url = it
                    urlError = false
                },
                placeholder = "https://ejemplo.com",
                isError = urlError
            )

            Text(
                text = "TÍTULO",
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = InkMuted,
                letterSpacing = 0.4.sp,
                modifier = Modifier.padding(bottom = 7.dp)
            )
            FieldInput(
                value = title,
                onValueChange = { title = it },
                placeholder = "Nombre del enlace"
            )

            Text(
                text = "ETIQUETA",
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = InkMuted,
                letterSpacing = 0.4.sp,
                modifier = Modifier.padding(bottom = 7.dp)
            )
            FieldInput(
                value = tag,
                onValueChange = { tag = it },
                placeholder = "Diseño, IA, Dev…"
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Background)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cancelar",
                        fontFamily = Manrope,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = InkMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(2f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Accent)
                        .clickable {
                            if (url.isBlank()) {
                                urlError = true
                            } else {
                                val link = Link(
                                    id = editingLink?.id ?: UUID.randomUUID(),
                                    title = title.ifBlank { url },
                                    url = url,
                                    emoji = selectedEmoji,
                                    bgColor = selectedColor,
                                    tag = tag,
                                    createdAt = editingLink?.createdAt ?: "Ahora mismo",
                                    modifiedAt = "Ahora mismo"
                                )
                                onSave(link)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (editingLink != null) "Guardar cambios" else "Guardar",
                        fontFamily = Manrope,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Surface
                    )
                }
            }
        }
    }
}
