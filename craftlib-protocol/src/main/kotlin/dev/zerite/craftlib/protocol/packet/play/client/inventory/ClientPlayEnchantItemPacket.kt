package dev.zerite.craftlib.protocol.packet.play.client.inventory

import dev.zerite.craftlib.protocol.Packet
import dev.zerite.craftlib.protocol.PacketIO
import dev.zerite.craftlib.protocol.ProtocolBuffer
import dev.zerite.craftlib.protocol.connection.NettyConnection
import dev.zerite.craftlib.protocol.version.ProtocolVersion

/**
 * Enchant Item
 *
 * @author ChachyDev
 * @since 0.1.0-SNAPSHOT
 */
data class ClientPlayEnchantItemPacket(
    var windowId: Int,
    var enchantment: Int
) : Packet() {
    companion object : PacketIO<ClientPlayEnchantItemPacket> {
        override fun read(
            buffer: ProtocolBuffer,
            version: ProtocolVersion,
            connection: NettyConnection
        ) = ClientPlayEnchantItemPacket(
            buffer.readByte().toInt(),
            buffer.readByte().toInt()
        )

        override fun write(
            buffer: ProtocolBuffer,
            version: ProtocolVersion,
            packet: ClientPlayEnchantItemPacket,
            connection: NettyConnection
        ) {
            buffer.writeByte(packet.windowId)
            buffer.writeByte(packet.enchantment)
        }
    }
}
