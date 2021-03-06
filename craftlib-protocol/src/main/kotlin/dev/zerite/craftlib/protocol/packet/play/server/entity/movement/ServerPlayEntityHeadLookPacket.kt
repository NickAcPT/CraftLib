package dev.zerite.craftlib.protocol.packet.play.server.entity.movement

import dev.zerite.craftlib.protocol.Packet
import dev.zerite.craftlib.protocol.PacketIO
import dev.zerite.craftlib.protocol.ProtocolBuffer
import dev.zerite.craftlib.protocol.connection.NettyConnection
import dev.zerite.craftlib.protocol.packet.base.EntityIdPacket
import dev.zerite.craftlib.protocol.version.ProtocolVersion

/**
 * Sent by the server to indicate that an entity is looking in a
 * specific direction.
 *
 * @author Koding
 * @since  0.1.0-SNAPSHOT
 */
data class ServerPlayEntityHeadLookPacket(
    override var entityId: Int,
    var headYaw: Float
) : EntityIdPacket, Packet() {
    companion object : PacketIO<ServerPlayEntityHeadLookPacket> {
        override fun read(
            buffer: ProtocolBuffer,
            version: ProtocolVersion,
            connection: NettyConnection
        ) = ServerPlayEntityHeadLookPacket(
            if (version >= ProtocolVersion.MC1_8) buffer.readVarInt() else buffer.readInt(),
            buffer.readStepRotation()
        )

        override fun write(
            buffer: ProtocolBuffer,
            version: ProtocolVersion,
            packet: ServerPlayEntityHeadLookPacket,
            connection: NettyConnection
        ) {
            if (version >= ProtocolVersion.MC1_8) buffer.writeVarInt(packet.entityId)
            else buffer.writeInt(packet.entityId)
            buffer.writeStepRotation(packet.headYaw)
        }
    }
}
