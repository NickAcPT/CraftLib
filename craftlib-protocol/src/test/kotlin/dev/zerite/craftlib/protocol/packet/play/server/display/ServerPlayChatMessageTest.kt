package dev.zerite.craftlib.protocol.packet.play.server.display



import dev.zerite.craftlib.protocol.packet.PacketTest
import dev.zerite.craftlib.protocol.version.ProtocolVersion
import net.kyori.adventure.text.format.NamedTextColor

/**
 * Tests that the chat message packet is properly reading and writing the
 * raw message JSON.
 *
 * @author Koding
 * @since  0.1.0-SNAPSHOT
 */
class ServerPlayChatMessageTest : PacketTest<ServerPlayChatMessagePacket>(ServerPlayChatMessagePacket) {

    init {
        example(ServerPlayChatMessagePacket(net.kyori.adventure.text.TextComponent.of("placeholder")))
        example(ServerPlayChatMessagePacket(net.kyori.adventure.text.TextComponent.of("Sample Text")))
        example(ServerPlayChatMessagePacket(net.kyori.adventure.text.TextComponent.of("writing test").color(NamedTextColor.BLUE))) {
            ProtocolVersion.MC1_7_2 {
                writeChat(net.kyori.adventure.text.TextComponent.of("writing test").color(NamedTextColor.BLUE))
            }
            ProtocolVersion.MC1_8 {
                writeChat(net.kyori.adventure.text.TextComponent.of("writing test").color(NamedTextColor.BLUE))
                writeByte(0)
            }
        }
    }

}
