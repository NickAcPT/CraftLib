package dev.zerite.craftlib.protocol.packet.base

/**
 * Simple interface which houses an entity ID which has been
 * included in the associated packet.
 *
 * @author Koding
 * @since  0.1.0-SNAPSHOT
 */
interface EntityIdPacket {
    /**
     * The entity ID which was read from the packet.
     */
    var entityId: Int
}