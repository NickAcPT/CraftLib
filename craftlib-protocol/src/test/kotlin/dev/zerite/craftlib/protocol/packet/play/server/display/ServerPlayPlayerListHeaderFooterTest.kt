package dev.zerite.craftlib.protocol.packet.play.server.display

import net.kyori.adventure.text.TextComponent
import dev.zerite.craftlib.protocol.packet.PacketTest
import dev.zerite.craftlib.protocol.version.ProtocolVersion

/**
 * Updates the player list header and footer for the client.
 *
 * @author Koding
 * @since  0.1.1-SNAPSHOT
 */
class ServerPlayPlayerListHeaderFooterTest :
    PacketTest<ServerPlayPlayerListHeaderFooterPacket>(ServerPlayPlayerListHeaderFooterPacket) {
    init {
        example(ServerPlayPlayerListHeaderFooterPacket(TextComponent.of("example"), TextComponent.of("text"))) {
            ProtocolVersion.MC1_8 {
                writeString("{\"text\":\"example\"}")
                writeString("{\"text\":\"text\"}")
            }
        }
    }
}
