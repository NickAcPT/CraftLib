package io.github.nickacpt.craftlib.protocol.gen.model

import com.squareup.moshi.Json
import java.io.File


data class PacketInstruction(
    val field: String?,
    val value: String?,
    val operation: InstructionOperation,
    val type: InstructionFieldType?,
    val instructions: List<PacketInstruction>?,
    val condition: String?
)

data class ProtocolDataJson(val packets: PacketsDataJson)

data class PacketDataJson(
    val id: Int,
    val `class`: String,
    val direction: PacketDirection,
    val state: ProtocolState,
    val instructions: List<PacketInstruction>
) {
    fun toData(): PacketData {
        return PacketData(id, File(`class`).nameWithoutExtension, direction.inverse, state, instructions).apply {
            valid = inferPacketFields()
        }
    }

}

data class PacketsDataJson(@Json(name = "packet") val packetList: Map<String, PacketDataJson>)

