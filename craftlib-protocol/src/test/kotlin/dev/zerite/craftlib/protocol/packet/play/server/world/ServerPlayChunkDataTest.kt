package dev.zerite.craftlib.protocol.packet.play.server.world

import dev.zerite.craftlib.commons.world.Block
import dev.zerite.craftlib.protocol.data.world.Chunk
import dev.zerite.craftlib.protocol.data.world.ChunkColumn
import dev.zerite.craftlib.protocol.packet.PacketTest
import dev.zerite.craftlib.protocol.util.ext.deflated
import dev.zerite.craftlib.protocol.version.ProtocolVersion

/**
 * Chunks are not unloaded by the client automatically. To unload chunks, send this
 * packet with ground-up continuous=true and no 16^3 chunks (eg. primary bit mask=0).
 *
 * The server does not send skylight information for nether-chunks, it's up to the client
 * to know if the player is currently in the nether. You can also infer this information from
 * the primary bitmask and the amount of uncompressed bytes sent.
 *
 * @author Koding
 * @since  0.1.0-SNAPSHOT
 */
class ServerPlayChunkDataTest : PacketTest<ServerPlayChunkDataPacket>(ServerPlayChunkDataPacket) {
    init {
        example(
            ServerPlayChunkDataPacket(ChunkColumn(42, 42, Array(16) { Chunk() }, ByteArray(16 * 16) { 1 }), true),
            maximumVersion = ProtocolVersion.MC1_8
        )
        example(
            ServerPlayChunkDataPacket(
                ChunkColumn(
                    16,
                    16,
                    Array(16) {
                        Chunk().apply {
                            this[0, 0, 0] = Block(0, 7, 7, 8)
                            this[0, 1, 0] = Block(1, 2, 3, 4)
                        }
                    },
                    ByteArray(16 * 16) { 7 }
                ),
                true
            )
        )

        val column = ChunkColumn(20, 21, Array(16) { Chunk() }, byteArrayOf())
        example(ServerPlayChunkDataPacket(column, true)) {
            ProtocolVersion.MC1_7_2 {
                val bytes = ChunkColumn.writeOneSeven(column).output.deflated()

                // Coords
                writeInt(20)
                writeInt(21)
                writeBoolean(true)

                // Masks
                writeShort(0)
                writeShort(0)

                // Compressed data
                writeInt(bytes.size)
                writeBytes(bytes)
            }
            ProtocolVersion.MC1_8 {
                val bytes = ChunkColumn.writeOneEight(column).output
                writeInt(20)
                writeInt(21)
                writeBoolean(true)
                writeShort(0)
                writeVarInt(bytes.size)
                writeBytes(bytes)
            }
        }
    }
}
