@file:JvmName("JsonUtil")
package dev.zerite.craftlib.protocol.util.ext

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.zerite.craftlib.protocol.util.json.factory.ProtocolVersionTypeAdapter
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

/**
 * Reference to the singular gson instance which we use.
 */
val gson: Gson = GsonComponentSerializer.gson().serializer().newBuilder()
    .registerTypeAdapterFactory(ProtocolVersionTypeAdapter)
    .create()
