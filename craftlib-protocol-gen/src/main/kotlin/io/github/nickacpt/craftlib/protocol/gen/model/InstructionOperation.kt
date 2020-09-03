package io.github.nickacpt.craftlib.protocol.gen.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

enum class InstructionOperation(val jsonValue: String) {
    WRITE("write"),
    STORE("store"),
    IF("if"),
    UNKNOWN("unknown")
}

object InstructionOperationAdapter {
    @FromJson
    fun fromJson(value: String): InstructionOperation {
        return InstructionOperation.values().firstOrNull { it.jsonValue == value } ?: InstructionOperation.UNKNOWN
    }

    @ToJson
    fun toJson(value: InstructionOperation): String {
        return value.jsonValue
    }
}


object InstructionFieldTypeAdapter {
    @FromJson
    fun fromJson(value: String): InstructionFieldType {
        return InstructionFieldType.values().firstOrNull { (it.jsonName ?: it.name).equals(value, true) } ?: InstructionFieldType.UNKNOWN
    }

    @ToJson
    fun toJson(value: InstructionFieldType): String {
        return value.jsonName ?: value.name.toLowerCase()
    }
}
