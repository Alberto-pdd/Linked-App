package pdalbert.apps.linked.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class FolderTag(
    @Serializable(with = UUIDSerializer::class)
    val folderId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val tagId: UUID
)
