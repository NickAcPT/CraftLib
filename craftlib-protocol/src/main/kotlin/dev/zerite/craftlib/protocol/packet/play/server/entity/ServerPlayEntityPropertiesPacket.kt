package dev.zerite.craftlib.protocol.packet.play.server.entity

import dev.zerite.craftlib.protocol.Packet
import dev.zerite.craftlib.protocol.PacketIO
import dev.zerite.craftlib.protocol.ProtocolBuffer
import dev.zerite.craftlib.protocol.connection.NettyConnection
import dev.zerite.craftlib.protocol.data.registry.RegistryEntry
import dev.zerite.craftlib.protocol.data.registry.impl.MagicEntityProperty
import dev.zerite.craftlib.protocol.packet.base.EntityIdPacket
import dev.zerite.craftlib.protocol.version.ProtocolVersion
import java.util.*

/**
 * Sent by the server to tell clients about any additional properties which may
 * be attached to an entity not covered by entity metadata. These include properties
 * like walking speed, flying speed etc.
 *
 * @author Koding
 * @since  0.1.0-SNAPSHOT
 */
data class ServerPlayEntityPropertiesPacket(
    override var entityId: Int,
    var properties: Array<Property>
) : EntityIdPacket, Packet() {

    companion object : PacketIO<ServerPlayEntityPropertiesPacket> {
        override fun read(
            buffer: ProtocolBuffer,
            version: ProtocolVersion,
            connection: NettyConnection
        ) = ServerPlayEntityPropertiesPacket(
            if (version >= ProtocolVersion.MC1_8) buffer.readVarInt() else buffer.readInt(),
            buffer.readArray({ readInt() }) {
                Property(
                    MagicEntityProperty[version, readString()],
                    readDouble(),
                    readArray({ if (version >= ProtocolVersion.MC1_8) readVarInt() else readShort().toInt() }) {
                        Modifier(
                            buffer.readUUID(ProtocolBuffer.UUIDMode.RAW),
                            buffer.readDouble(),
                            buffer.readByte()
                        )
                    }
                )
            }
        )

        override fun write(
            buffer: ProtocolBuffer,
            version: ProtocolVersion,
            packet: ServerPlayEntityPropertiesPacket,
            connection: NettyConnection
        ) {
            if (version >= ProtocolVersion.MC1_8) buffer.writeVarInt(packet.entityId)
            else buffer.writeInt(packet.entityId)
            buffer.writeArray(packet.properties, { writeInt(it) }) { p ->
                writeString(MagicEntityProperty[version, p.key, String::class.java] ?: return@writeArray)
                writeDouble(p.value)
                writeArray(p.modifiers, { if (version >= ProtocolVersion.MC1_8) writeVarInt(it) else writeShort(it) }) {
                    writeUUID(it.uuid, ProtocolBuffer.UUIDMode.RAW)
                    writeDouble(it.amount)
                    writeByte(it.operation.toInt())
                }
            }
        }
    }

    data class Property(
        var key: RegistryEntry,
        var value: Double,
        var modifiers: Array<Modifier>
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Property

            if (key != other.key) return false
            if (value != other.value) return false
            if (!modifiers.contentEquals(other.modifiers)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = key.hashCode()
            result = 31 * result + value.hashCode()
            result = 31 * result + modifiers.contentHashCode()
            return result
        }
    }

    data class Modifier(
        var uuid: UUID,
        var amount: Double,
        var operation: Byte
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServerPlayEntityPropertiesPacket

        if (entityId != other.entityId) return false
        if (!properties.contentEquals(other.properties)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = entityId
        result = 31 * result + properties.contentHashCode()
        return result
    }

}
