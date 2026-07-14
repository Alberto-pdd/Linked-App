package pdalbert.apps.linked.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pdalbert.apps.linked.data.model.Folder
import pdalbert.apps.linked.ui.components.ColorPicker
import pdalbert.apps.linked.ui.components.EmojiPicker
import pdalbert.apps.linked.ui.theme.Accent
import pdalbert.apps.linked.ui.theme.Background
import pdalbert.apps.linked.ui.theme.Border
import pdalbert.apps.linked.ui.theme.Ink
import pdalbert.apps.linked.ui.theme.InkMuted
import pdalbert.apps.linked.ui.theme.Manrope
import pdalbert.apps.linked.ui.theme.Surface
import java.util.UUID

private val folderEmojis = listOf(
    "\uD83D\uDCC1", "\uD83D\uDCC2", "\uD83C\uDFE0", "\uD83D\uDCBC", "\u2708\uFE0F",
    "\uD83D\uDCDA", "\uD83C\uDFAE", "\uD83C\uDFB5", "\uD83C\uDF55", "\uD83D\uDCA1",
    "\uD83D\uDD2C", "\uD83C\uDFA8", "\uD83D\uDEE0\uFE0F", "\u2B50", "\uD83C\uDF10"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFolderSheet(
    editingFolder: Folder? = null,
    onDismiss: () -> Unit,
    onSave: (Folder) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedEmoji by remember { mutableStateOf(editingFolder?.emoji ?: "\uD83D\uDCC1") }
    var selectedColor by remember { mutableStateOf(editingFolder?.bgColor ?: "#FEF3C7") }
    var name by remember { mutableStateOf(editingFolder?.name ?: "") }

    var nameError by remember { mutableStateOf(false) }

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
                text = if (editingFolder != null) "Editar carpeta" else "Nueva carpeta",
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
                emojis = folderEmojis,
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
                text = "NOMBRE",
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = InkMuted,
                letterSpacing = 0.4.sp,
                modifier = Modifier.padding(bottom = 7.dp)
            )
            FieldInput(
                value = name,
                onValueChange = {
                    name = it
                    nameError = false
                },
                placeholder = "Ej: Trabajo, Viajes, Recursos…",
                isError = nameError
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
                            if (name.isBlank()) {
                                nameError = true
                            } else {
                                val folder = Folder(
                                    id = editingFolder?.id ?: UUID.randomUUID(),
                                    name = name,
                                    emoji = selectedEmoji,
                                    bgColor = selectedColor
                                )
                                onSave(folder)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (editingFolder != null) "Guardar cambios" else "Guardar",
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

@Composable
internal fun FieldInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false
) {
    val borderColor = when {
        isError -> Color(0xFFEF4444)
        else -> Border
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(Background)
            .border(1.5.dp, borderColor, RoundedCornerShape(13.dp))
            .padding(horizontal = 13.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                fontFamily = Manrope,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = InkMuted
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontFamily = Manrope,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Ink
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }

    Spacer(modifier = Modifier.height(13.dp))
}
