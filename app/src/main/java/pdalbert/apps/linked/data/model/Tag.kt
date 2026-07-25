package pdalbert.apps.linked.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Tag(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val colorName: String = TagColor.DEFAULT.name
)
