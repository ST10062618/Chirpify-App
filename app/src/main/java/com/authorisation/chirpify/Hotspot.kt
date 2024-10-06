package com.authorisation.chirpify


import android.content.Context

data class Hotspot(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val description: String
)
data class HotspotResponse(
    val hotspots: List<Hotspot>
)
  class HotspotsRepository(private val context: Context) {

    fun fetchEBirdHotspots(): List<Hotspot> {
        // Replace with actual API call to get eBird hotspots
        return listOf(
            Hotspot("eBird Hotspot 1", -26.2041, 28.0473, "Description for Hotspot 1"),
            Hotspot("eBird Hotspot 2", -26.2141, 28.0573, "Description for Hotspot 2")
        )
    }

    fun fetchUserHotspots(): List<Hotspot> {
        val sharedPreferences = context.getSharedPreferences("BirdHotspots", Context.MODE_PRIVATE)
        val userHotspots = mutableListOf<Hotspot>()

        val hotspotsString = sharedPreferences.getString("userHotspots", "") ?: ""
        val hotspotsArray = hotspotsString.split(";")

        for (hotspot in hotspotsArray) {
            if (hotspot.isNotEmpty()) {
                val parts = hotspot.split(",")
                if (parts.size == 4) { // Adjusted for the new data class
                    userHotspots.add(Hotspot(parts[0], parts[1].toDouble(), parts[2].toDouble(), parts[3]))
                }
            }
        }
        return userHotspots
    }

    fun addUserHotspot(hotspot: Hotspot) {
        val sharedPreferences = context.getSharedPreferences("BirdHotspots", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val existingHotspots = sharedPreferences.getString("userHotspots", "") ?: ""
        val newHotspotString = "${hotspot.name},${hotspot.latitude},${hotspot.longitude},${hotspot.description};"
        editor.putString("userHotspots", existingHotspots + newHotspotString)
        editor.apply()
    }
}

