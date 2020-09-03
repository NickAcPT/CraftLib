package io.github.nickacpt.craftlib.protocol.gen.model

import io.github.nickacpt.craftlib.protocol.gen.snakeToLowerCamelCase

data class PacketData(
    val id: Int,
    val name: String,
    val direction: PacketDirection,
    val state: ProtocolState,
    val instructions: List<PacketInstruction>
) {
    val fields: MutableList<PacketField> = mutableListOf()

    fun inferPacketFields() {
        val instructions = instructions
        instructions
            .forEach { instruction ->
                instructionToField(instruction, instructions)?.let { fields.add(it) }
            }
    }

    val fieldValueMap = mutableListOf(
        listOf("SerializableUUID", "uuidToIntArray") to InstructionFieldType.UUID,
        listOf("GSON", "toJson") to InstructionFieldType.JSON

    )

    private fun instructionToField(
        instruction: PacketInstruction,
        allInstructions: List<PacketInstruction>
    ): PacketField? {
        if (instruction.operation == InstructionOperation.WRITE) {
            var fieldName = instruction.field ?: return null
            skipFieldsWithSuffixes.forEachIndexed { _, suffix ->
                if (fieldName.endsWith(suffix) && allInstructions.any { i ->
                        i.field?.equals(
                            fieldName.removeSuffix(
                                suffix
                            )
                        ) == true
                    }
                ) return null
            }

            unwantedSuffixes.forEach { suffix ->
                fieldName = fieldName.removeSuffix(suffix)
            }

            fieldName = fixFieldNameCalls(fieldName)
            var attemptCount = 5
            while (--attemptCount > 0 && fieldName.contains('.')) {
                fieldName = fieldName.replace(getterAccessRegex, getterAccessReplacer).snakeToLowerCamelCase()
            }
            if (attemptCount == 0) {
                fieldName = "var${fields.size + 1}"
            }

            val type = instruction.type ?: return null

            return PacketField(fieldName, type, type.asConstructorType)
        } else if (instruction.operation == InstructionOperation.STORE) {
            val rawFieldValue = instruction.value ?: return null
            var fieldValue = rawFieldValue
            if (!fieldValue.contains("this.")) return null

            fieldValue = fixFieldNameCalls(fieldValue)

            fieldValue = mutableListOf<String>().also {
                val matcher = getterAccessPattern.matcher(fieldValue)
                while (matcher.find()) {
                    it.add(matcher.group())
                }
            }.lastOrNull() ?: return null
            fieldValue = fieldValue.replace(getterAccessRegex, getterAccessReplacer).snakeToLowerCamelCase()

            var type = instruction.type ?: return null
            fieldValueMap.forEach { it ->
                if (it.first.any { rawFieldValue.contains(it) }) {
                    type = it.second
                }
            }
            return PacketField(fieldValue, type, type.asConstructorType)

        } else if (instruction.operation == InstructionOperation.IF) {
            val condition = instruction.condition ?: return null
            val instructions = instruction.instructions ?: return null
            val instructionsAsFields = instructions.mapNotNull { instructionToField(it, instructions) }

            val notNullSuffix = " != null"
            //We hit an optional value
            if (condition.endsWith(notNullSuffix)) {
                return instructionsAsFields.firstOrNull { it.name == condition.removeSuffix(notNullSuffix) }
                    ?.copy(isOptional = true)
            }
        }

        return null
    }

    private fun fixFieldNameCalls(fieldName: String): String {
        var name = fieldName
        if (complexGetterAccessRegex.containsMatchIn(name)) {
            name = complexGetterAccessPattern.matcher(name).also { it.find() }.group(1)
        }
        return name
    }
}
