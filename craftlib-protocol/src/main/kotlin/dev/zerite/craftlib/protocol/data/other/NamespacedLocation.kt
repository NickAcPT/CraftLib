package dev.zerite.craftlib.protocol.data.other

data class NamespacedLocation(val namespace: String, val location: String) {
    companion object {
        @JvmStatic
        fun fromString(string: String): NamespacedLocation {
            string.let {
                val split = it.split(":")
                return NamespacedLocation(split[0], split[1])
            }
        }
    }
    override fun toString(): String = "$namespace:$location"
}