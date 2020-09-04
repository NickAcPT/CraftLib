package io.github.nickacpt.craftlib.protocol.gen.model

import com.squareup.kotlinpoet.TypeName
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import io.github.nickacpt.craftlib.protocol.gen.ProtocolAnnotationProcessor
import javax.tools.Diagnostic

data class ProtocolData(val packets: List<PacketData>)

object ProtocolDataAdapter {
    @FromJson
    fun dataFromJson(data: ProtocolDataJson): ProtocolData {
        return ProtocolData(data.packets.packetList.map { it.value.toData() }.filter {
            if (!it.valid) ProtocolAnnotationProcessor.env.messager.printMessage(
                Diagnostic.Kind.WARNING,
                "Skipping ${it.name} because it contains invalid fields\n"
            )
            it.valid
        }.toList())
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
) {
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

    val inverse: PacketDirection
        get() = when(this) {
            CLIENTBOUND -> SERVERBOUND
            SERVERBOUND -> CLIENTBOUND
        }

    val asPackage = name.toLowerCase().removeSuffix("bound")
}