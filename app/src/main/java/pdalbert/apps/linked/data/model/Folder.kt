package pdalbert.apps.linked.data.model

import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class Folder(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val emoji: String = "\uD83D\uDCC1",
    val bgColor: String = "#FEF3C7",
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant = Instant.parse("2024-01-01T00:00:00Z")
)
