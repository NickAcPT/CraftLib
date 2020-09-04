package io.github.nickacpt.craftlib.protocol.gen.codegen

import com.squareup.kotlinpoet.ClassName

val packetClass = ClassName("dev.zerite.craftlib.protocol", "Packet")

val chatComponentType = ClassName("net.kyori.adventure.text", "Component")

val packetIoType = ClassName("dev.zerite.craftlib.protocol", "PacketIO")

val protocolBufferType = ClassName("dev.zerite.craftlib.protocol", "ProtocolBuffer")

val protocolVersionType = ClassName("dev.zerite.craftlib.protocol.version", "ProtocolVersion")

val nettyConnectionType = ClassName("dev.zerite.craftlib.protocol.connection", "NettyConnection")

val uuidType = ClassName("java.util", "UUID")

val namespacedLocationType = ClassName("dev.zerite.craftlib.protocol.data.other", "NamespacedLocation")

val protocolStateType = ClassName("dev.zerite.craftlib.protocol.version", "ProtocolState")

val abstractProtocolType = ClassName("dev.zerite.craftlib.protocol.version", "AbstractProtocol")

val vector3Type = ClassName("dev.zerite.craftlib.protocol", "Vector3")

val slotType = ClassName("dev.zerite.craftlib.protocol", "Slot")
