package io.github.nickacpt.craftlib.protocol.gen.model

import com.squareup.kotlinpoet.TypeName
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

data class ProtocolData(val packets: List<PacketData>)

object ProtocolDataAdapter {
    @FromJson
    fun dataFromJson(data: ProtocolDataJson): ProtocolData {
        return ProtocolData(data.packets.packetList.map { it.value.toData() }.toList())
    }

    @ToJson
    fun dataToJson(data: ProtocolData): ProtocolDataJson? {
        return null
    }

}

data class PacketField(
    val name: String,
    val rawType: InstructionFieldType,
    var type: TypeName,
    val isOptional: Boolean = false
)
{
    init {
        if (isOptional)
            type = type.copy(true)
    }
}

enum class ProtocolState(val id: Int, friendlyName: String? = null) {
    HANDSHAKING(-1, "HANDSHAKE"),
    PLAY(0),
    STATUS(1),
    LOGIN(2);

    val actualName = friendlyName ?: name
}

enum class PacketDirection {
    CLIENTBOUND,
    SERVERBOUND;

    val asPackage = name.toLowerCase().removeSuffix("bound")
}