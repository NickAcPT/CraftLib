package io.github.nickacpt.craftlib.protocol.gen.model

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.BYTE_ARRAY
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName
import io.github.nickacpt.craftlib.protocol.gen.codegen.*

enum class InstructionFieldType(
    val methodSuffix: String,
    existingWriteMethod: String? = null,
    existingReadMethod: String? = null,
    val jsonName: String? = null,
    val writeSuffixArgs: Array<CodeBlock> = emptyArray(),
    val readSuffixArgs: Array<CodeBlock> = emptyArray(),
) {
    INT("Int"),
    VARINT("VarInt"),
    STRING("String"),
    VARSHORT("VarShort"),
    SHORT("Short", existingReadMethod = "readUnsignedShort"),
    CHATCOMPONENT("Chat"),
    JSON(methodSuffix = "Json"),
    LONG(methodSuffix = "Long"),
    BYTEARRAY(
        jsonName = "byte[]", methodSuffix = "ByteArray",
        writeSuffixArgs = arrayOf(CodeBlock.of("{ if (version >= ProtocolVersion.MC1_8) writeVarInt(it) else writeShort(it) }")),
        readSuffixArgs = arrayOf(
            CodeBlock.of("null"),
            CodeBlock.of("{ if (version >= ProtocolVersion.MC1_8) readVarInt() else readShort().toInt() }")
        ),
    ),
    UUID(methodSuffix = "UUID"),
    IDENTIFIER(methodSuffix = "Identifier"),
    BYTE(methodSuffix = "Byte", existingReadMethod = "readByteAsInt"),
    DOUBLE(methodSuffix = "Double"),
    ENUM(methodSuffix = "VarInt"),
    POSITION(methodSuffix = "Position"),
    BOOLEAN(methodSuffix = "Boolean"),
    ITEMSTACK(methodSuffix = "Slot"),
    FLOAT(methodSuffix = "Float"),
    VARLONG(methodSuffix = "VarLong"),
    UNKNOWN("");

    val writeMethod = existingWriteMethod ?: "write${methodSuffix}"
    val readMethod = existingReadMethod ?: "read${methodSuffix}"

    val asConstructorType: TypeName by lazy {
        when (this) {
            ENUM, BYTE, INT, VARINT -> com.squareup.kotlinpoet.INT
            STRING -> com.squareup.kotlinpoet.STRING
            VARSHORT, SHORT -> com.squareup.kotlinpoet.INT
            CHATCOMPONENT -> chatComponentType
            JSON, UNKNOWN -> ANY
            BYTEARRAY -> BYTE_ARRAY
            UUID -> uuidType
            IDENTIFIER -> namespacedLocationType
            VARLONG, LONG -> com.squareup.kotlinpoet.LONG
            DOUBLE -> com.squareup.kotlinpoet.DOUBLE
            POSITION -> vector3Type
            BOOLEAN -> com.squareup.kotlinpoet.BOOLEAN
            ITEMSTACK -> slotType
            FLOAT -> com.squareup.kotlinpoet.FLOAT
        }
    }
}