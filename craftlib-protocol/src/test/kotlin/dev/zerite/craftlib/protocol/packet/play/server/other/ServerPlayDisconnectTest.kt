package dev.zerite.craftlib.protocol.packet.play.server.other

import net.kyori.adventure.text.TextComponent
import dev.zerite.craftlib.protocol.packet.PacketTest
import dev.zerite.craftlib.protocol.version.ProtocolVersion

/**
 * Sent by the server before it disconnects a client. The server assumes that the sender has
 * already closed the connection by the time the packet arrives.
 *
 * @author Koding
 * @since  0.1.0-SNAPSHOT
 */
class ServerPlayDisconnectTest : PacketTest<ServerPlayDisconnectPacket>(ServerPlayDisconnectPacket) {

    init {
        example(ServerPlayDisconnectPacket(TextComponent.of("Example"))) {
            ProtocolVersion.MC1_7_2 {
                writeString("{\"text\":\"Example\"}")
            }
        }
        example(ServerPlayDisconnectPacket(TextComponent.of("Quitting"))) {
            ProtocolVersion.MC1_7_2 {
                writeString("{\"text\":\"Quitting\"}")
            }
        }
    }

}
