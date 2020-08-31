package dev.zerite.craftlib.protocol.util.ext

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

val Component.unformattedText: String
    get() {
        return LegacyComponentSerializer.legacySection().serialize(this)
    }

val String.chatComponent: Component
    get() {
        return GsonComponentSerializer.gson().deserialize(this)
    }

val Component.json: String
    get() {
        return GsonComponentSerializer.gson().serialize(this)
    }