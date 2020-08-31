package dev.zerite.craftlib.protocol.packet.play.client.other

import dev.zerite.craftlib.protocol.Vector3
import dev.zerite.craftlib.protocol.packet.PacketTest
import dev.zerite.craftlib.protocol.version.ProtocolVersion
import net.kyori.adventure.text.TextComponent

/**
 * This message is sent from the client to the server when the "Done" button is pushed after placing a sign.
 *
 * @author ChachyDev
 * @since 0.1.0-SNAPSHOT
 */
class ClientPlayUpdateSignTest : PacketTest<ClientPlayUpdateSignPacket>(ClientPlayUpdateSignPacket) {
    init {
        example(
            ClientPlayUpdateSignPacket(
                130,
                90,
                130,
                TextComponent.of("This"),
                TextComponent.of("is"),
                TextComponent.of("a"),
                TextComponent.of("Test")
            )
        ) {
            ProtocolVersion.MC1_7_2 {
                writeInt(130)
                writeShort(90)
                writeInt(130)
                writeString("This")
                writeString("is")
                writeString("a")
                writeString("Test")
            }
            ProtocolVersion.MC1_8 {
                writePosition(Vector3(130, 90, 130))
                writeString("{\"text\":\"This\"}")
                writeString("{\"text\":\"is\"}")
                writeString("{\"text\":\"a\"}")
                writeString("{\"text\":\"Test\"}")
            }
        }
    }
}
