package dev.zerite.craftlib.protocol.data.other

import dev.zerite.craftlib.chat.component.BaseChatComponent
import dev.zerite.craftlib.chat.component.StringChatComponent
import dev.zerite.craftlib.protocol.version.ProtocolVersion

/**
 * Response which is sent in the status response packet to display
 * data in the server list.
 *
 * @author Koding
 * @since  0.1.0-SNAPSHOT
 */
data class StatusResponse @JvmOverloads constructor(
    var version: StatusVersion = StatusVersion("CraftLib", ProtocolVersion.MC1_7_2),
    var players: StatusPlayers = StatusPlayers(1, 0),
    var description: BaseChatComponent = StringChatComponent(""),
    var favicon: String? = null
)

/**
 * The version which is displayed to users.
 *
 * @author Koding
 * @since  0.1.0-SNAPSHOT
 */
data class StatusVersion(
    var name: String,
    var protocol: ProtocolVersion
)

/**
 * Shows a preview to players of others connected to the server.
 *
 * @author Koding
 * @since  0.1.0-SNAPSHOT
 */
data class StatusPlayers @JvmOverloads constructor(
    var max: Int,
    var online: Int,
    var sample: Array<StatusPlayer>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StatusPlayers

        if (max != other.max) return false
        if (online != other.online) return false
        if (sample != null) {
            if (other.sample == null) return false
            if (!sample!!.contentEquals(other.sample!!)) return false
        } else if (other.sample != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = max
        result = 31 * result + online
        result = 31 * result + (sample?.contentHashCode() ?: 0)
        return result
    }
}

/**
 * A single player which is connected to the server.
 *
 * @author Koding
 * @since  0.1.0-SNAPSHOT
 */
data class StatusPlayer(
    var name: String,
    var id: String
)
