package pdalbert.apps.linked.data.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

object UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())
}

object UUIDListSerializer : KSerializer<List<UUID>> {
    private val delegateSerializer = ListSerializer(UUIDSerializer)
    override val descriptor: SerialDescriptor = delegateSerializer.descriptor
    override fun serialize(encoder: Encoder, value: List<UUID>) {
        encoder.encodeSerializableValue(delegateSerializer, value)
    }
    override fun deserialize(decoder: Decoder): List<UUID> {
        return decoder.decodeSerializableValue(delegateSerializer)
    }
}
