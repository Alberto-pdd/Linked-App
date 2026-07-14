package pdalbert.apps.linked.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Link(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val url: String,
    val emoji: String = "\uD83D\uDD17",
    val bgColor: String = "#EBF3FB",
    val description: String = "",
    val tag: String = "",
    @Serializable(with = UUIDListSerializer::class)
    val folderIds: List<UUID> = emptyList(),
    val createdAt: String = "",
    val modifiedAt: String = ""
) {
    val tagColor: TagColor get() = tagColorFor(tag)
}
