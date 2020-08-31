package dev.zerite.craftlib.protocol.packet.play.server.display

import net.kyori.adventure.text.TextComponent
import dev.zerite.craftlib.protocol.data.registry.impl.MagicTitleAction
import dev.zerite.craftlib.protocol.packet.PacketTest
import dev.zerite.craftlib.protocol.version.ProtocolVersion

/**
 * Sent to the client to display a title to the player.
 *
 * @author Koding
 * @since  0.1.1-SNAPSHOT
 */
class ServerPlayTitleTest : PacketTest<ServerPlayTitlePacket>(ServerPlayTitlePacket) {
    init {
        example(ServerPlayTitlePacket(MagicTitleAction.TITLE, text = TextComponent.of("test"))) {
            ProtocolVersion.MC1_8 {
                writeVarInt(0)
                writeString("{\"text\":\"test\"}")
            }
        }
        example(ServerPlayTitlePacket(MagicTitleAction.TIMES)) {
            ProtocolVersion.MC1_8 {
                writeVarInt(2)
                writeInt(0)
                writeInt(0)
                writeInt(0)
            }
        }
        example(ServerPlayTitlePacket(MagicTitleAction.CLEAR)) {
            ProtocolVersion.MC1_8 {
                writeVarInt(3)
            }
        }
    }
}
