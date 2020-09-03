package io.github.nickacpt.craftlib.protocol.gen.codegen

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.github.nickacpt.craftlib.protocol.gen.model.PacketData
import io.github.nickacpt.craftlib.protocol.gen.model.PacketDirection
import io.github.nickacpt.craftlib.protocol.gen.removePrefixIgnoreCase
import java.io.File

class KlassGenerator(val `package`: String, val outputDir: File) {

    fun generate(packet: PacketData): TypeName {
        var typeName = packet.name
        PacketDirection.values().forEach { typeName = typeName.removePrefixIgnoreCase(it.name) }

        val typePackage = `package`
        FileSpec.builder(typePackage, typeName)
            .addType(TypeSpec.classBuilder(typeName).apply {
                addKdoc("${packet.state}_${packet.direction}_${packet.id.toString().padStart(2, '0')}")
                packet.fields.size.takeIf { it > 0 }?.also { addModifiers(KModifier.DATA) }
                superclass(packetClass)
                val type = this
                val packetClassName = ClassName(typePackage, typeName)

                primaryConstructor(FunSpec.constructorBuilder()
                    .apply {
                        packet.fields
                            .forEach {
                                val parameterName: String = it.name
                                val parameterType = it.type

                                addParameter(ParameterSpec.builder(parameterName, parameterType)
                                    .apply {
                                        if (it.isOptional) defaultValue("null")
                                    }
                                    .build())
                                type.addProperty(
                                    PropertySpec.builder(parameterName, parameterType).initializer(parameterName)
                                        .build()
                                )
                            }
                    }
                    .build()
                )

                addType(TypeSpec.companionObjectBuilder()
                    .apply {
                        addSuperinterface(packetIoType.parameterizedBy(packetClassName))

                        //Write function
                        addFunction(
                            createWriteFunction(packetClassName, packet)
                        )

                        //Read function
                        addFunction(
                            createReadFunction(packetClassName, packet)
                        )
                    }
                    .build())
            }.build()).build().writeTo(outputDir)

        return ClassName(typePackage, typeName)
    }

    private fun createReadFunction(
        packetClassName: ClassName,
        packet: PacketData
    ): FunSpec {
        return FunSpec.builder("read")
            .addModifiers(KModifier.OVERRIDE)
            .returns(packetClassName)
            .addParameter("buffer", protocolBufferType)
            .addParameter("version", protocolVersionType)
            .addParameter("connection", nettyConnectionType)
            .addCode(buildCodeBlock {

                val map = packet.fields.joinToString(",\n") { packetField ->
                    buildString {
                        val extraArgs = packetField.rawType.readSuffixArgs.joinToString(", ")

                        if (packetField.isOptional)
                            append("buffer.readOptional { ")
                        append("buffer.${packetField.rawType.readMethod}($extraArgs)")
                        if (packetField.isOptional)
                            append(" }")
                    }
                }
                addStatement("return %T($map)", packetClassName)
            })
            .build()
    }

    private fun createWriteFunction(
        packetClassName: ClassName,
        packet: PacketData
    ): FunSpec {
        return FunSpec.builder("write")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("buffer", protocolBufferType)
            .addParameter("version", protocolVersionType)
            .addParameter("packet", packetClassName)
            .addParameter("connection", nettyConnectionType)
            .addCode(buildCodeBlock {
                packet.fields.forEach { packetField ->
                    val extraArgs = packetField.rawType.writeSuffixArgs.joinToString(", ")
                        .let { if (it.isNotEmpty()) ", $it" else "" }

                    var fieldAccessor = "packet.${packetField.name}"
                    if (packetField.isOptional) {
                        addStatement("buffer.writeOptional($fieldAccessor) {")
                        indent()
                        fieldAccessor = "$fieldAccessor!!"
                    }
                    addStatement("buffer.${packetField.rawType.writeMethod}($fieldAccessor$extraArgs)")
                    if (packetField.isOptional) {
                        unindent()
                        addStatement("}")
                    }
                }
            })
            .build()
    }

}