package dev.zerite.craftlib.protocol.packet.play.server.interaction

import net.kyori.adventure.text.TextComponent
import dev.zerite.craftlib.protocol.Vector3
import dev.zerite.craftlib.protocol.packet.PacketTest
import dev.zerite.craftlib.protocol.version.ProtocolVersion

/**
 * This message is sent from the server to the client whenever a sign is discovered
 * or created. This message is NOT sent when a sign is destroyed or unloaded.
 *
 * @author Koding
 * @since  0.1.0-SNAPSHOT
 */
class ServerPlayUpdateSignTest : PacketTest<ServerPlayUpdateSignPacket>(ServerPlayUpdateSignPacket) {
    init {
        example(
            ServerPlayUpdateSignPacket(
                10,
                100,
                50,
                TextComponent.of("first"),
                TextComponent.of("second"),
                TextComponent.of("third"), TextComponent.of("forth")
            )
        ) {
            ProtocolVersion.MC1_7_2 {
                writeInt(10)
                writeShort(100)
                writeInt(50)
                writeString("first")
                writeString("second")
                writeString("third")
                writeString("forth")
            }
            ProtocolVersion.MC1_8 {
                writePosition(Vector3(10, 100, 50))
                writeChat(TextComponent.of("first"))
                writeChat(TextComponent.of("second"))
                writeChat(TextComponent.of("third"))
                writeChat(TextComponent.of("forth"))
            }
        }
        example(
            ServerPlayUpdateSignPacket(
                50,
                50,
                50,
                TextComponent.of("line"),
                TextComponent.of("line"),
                TextComponent.of("line"),
                TextComponent.of("line")
            )
        ) {
            ProtocolVersion.MC1_7_2 {
                writeInt(50)
                writeShort(50)
                writeInt(50)
                writeString("line")
                writeString("line")
                writeString("line")
                writeString("line")
            }
            ProtocolVersion.MC1_8 {
                writePosition(Vector3(50, 50, 50))
                writeChat(TextComponent.of("line"))
                writeChat(TextComponent.of("line"))
                writeChat(TextComponent.of("line"))
                writeChat(TextComponent.of("line"))
            }
        }
    }
}
