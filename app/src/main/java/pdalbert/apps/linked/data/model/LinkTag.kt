package pdalbert.apps.linked.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class LinkTag(
    @Serializable(with = UUIDSerializer::class)
    val linkId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val tagId: UUID
)
