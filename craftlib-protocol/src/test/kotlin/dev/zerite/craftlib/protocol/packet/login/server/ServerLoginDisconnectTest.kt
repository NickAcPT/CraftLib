package dev.zerite.craftlib.protocol.packet.login.server



import dev.zerite.craftlib.protocol.packet.PacketTest
import dev.zerite.craftlib.protocol.version.ProtocolVersion
import net.kyori.adventure.text.format.NamedTextColor

/**
 * Tests that the login disconnect packet is being written and read correctly.
 *
 * @author Koding
 * @since  0.1.0-SNAPSHOT
 */
class ServerLoginDisconnectTest : PacketTest<ServerLoginDisconnectPacket>(ServerLoginDisconnectPacket) {

    init {
        example(ServerLoginDisconnectPacket(net.kyori.adventure.text.TextComponent.of("Disconnected")))
        example(ServerLoginDisconnectPacket(net.kyori.adventure.text.TextComponent.of("Quitting").color(NamedTextColor.RED)))
        example(ServerLoginDisconnectPacket(net.kyori.adventure.text.TextComponent.of("Closing connection"))) {
            ProtocolVersion.MC1_7_2 {
                writeChat(net.kyori.adventure.text.TextComponent.of("Closing connection"))
            }
        }
    }

}