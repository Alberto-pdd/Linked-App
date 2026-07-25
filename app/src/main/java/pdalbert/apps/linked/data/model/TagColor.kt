package pdalbert.apps.linked.data.model

import androidx.compose.ui.graphics.Color

enum class TagColor(
    val bgColor: String,
    val fgColor: String,
    val borderColor: String
) {
    DEFAULT("#F2F0EC", "#1A1A1A", "#E0DED9"),
    YELLOW("#FFF9C4", "#7C6B00", "#E8D552"),
    LIME("#DCEDC8", "#33691E", "#A5D6A7"),
    GREEN("#C8E6C9", "#1B5E20", "#66BB6A"),
    TEAL("#B2DFDB", "#004D40", "#26A69A"),
    CYAN("#B2EBF2", "#006064", "#00BCD4"),
    BLUE("#BBDEFB", "#0D47A1", "#42A5F5"),
    INDIGO("#C5CAE9", "#1A237E", "#5C6BC0"),
    PURPLE("#E1BEE7", "#4A148C", "#AB47BC"),
    PINK("#F8BBD0", "#880E4F", "#EC407A"),
    RED("#FFCDD2", "#B71C1C", "#EF5350"),
    ORANGE("#FFE0B2", "#E65100", "#FF9800"),
    DEEP_ORANGE("#FFCCBC", "#BF360C", "#FF7043"),
    BROWN("#D7CCC8", "#3E2723", "#8D6E63"),
    GRAY("#CFD8DC", "#263238", "#78909C"),
    BLUE_GRAY("#E0E0E0", "#37474F", "#90A4AE")
}

fun TagColor.toBgColor(): Color = Color(android.graphics.Color.parseColor(bgColor))
fun TagColor.toFgColor(): Color = Color(android.graphics.Color.parseColor(fgColor))
fun TagColor.toBorderColor(): Color = Color(android.graphics.Color.parseColor(borderColor))

fun resolveTagColor(colorName: String): TagColor {
    return try {
        TagColor.valueOf(colorName)
    } catch (_: IllegalArgumentException) {
        TagColor.DEFAULT
    }
}
