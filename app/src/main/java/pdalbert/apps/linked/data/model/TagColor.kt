package pdalbert.apps.linked.data.model

import kotlin.math.abs

enum class TagColor {
    DEFAULT,
    YELLOW,
    GREEN,
    PURPLE
}

fun tagColorFor(tagName: String): TagColor {
    val colors = TagColor.entries.toTypedArray()
    return colors[abs(tagName.hashCode()) % colors.size]
}
