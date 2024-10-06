package com.authorisation.chirpify

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Data class for Hotspot
data class Hotspot(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val description: String
)

data class HotspotResponse(
    val hotspots: List<Hotspot>
)

// Retrofit API service
interface eBirdApiService {
    @GET("v2/hotspots") // Update with the actual eBird API endpoint
    suspend fun getHotspots(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("maxDistance") maxDistance: Int // Optional parameter
    ): HotspotResponse
}

// Hotspots Repository
class HotspotsRepository(private val context: Context) {

    private val apiService: eBirdApiService

    init {
        val EBIRD_API_TOKEN = "nc1meu0cup06" // Replace with your actual eBird API token

        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-eBirdApiToken", EBIRD_API_TOKEN)
                    .build()
                chain.proceed(request)
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.ebird.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(eBirdApiService::class.java)
    }

    // Fetch eBird hotspots
    suspend fun fetchEBirdHotspots(latitude: Double, longitude: Double, maxDistance: Int): List<Hotspot> {
        return try {
            val response = apiService.getHotspots(latitude, longitude, maxDistance)
            response.hotspots
        } catch (e: Exception) {
            emptyList() // Handle the error as needed
        }
    }

    // Fetch user hotspots
    fun fetchUserHotspots(): List<Hotspot> {
        val sharedPreferences = context.getSharedPreferences("BirdHotspots", Context.MODE_PRIVATE)
        val userHotspots = mutableListOf<Hotspot>()

        val hotspotsString = sharedPreferences.getString("userHotspots", "") ?: ""
        val hotspotsArray = hotspotsString.split(";")

        for (hotspot in hotspotsArray) {
            if (hotspot.isNotEmpty()) {
                val parts = hotspot.split(",")
                if (parts.size == 4) {
                    userHotspots.add(Hotspot(parts[0], parts[1].toDouble(), parts[2].toDouble(), parts[3]))
                }
            }
        }
        return userHotspots
    }

    // Add user hotspot
    fun addUserHotspot(hotspot: Hotspot) {
        val sharedPreferences = context.getSharedPreferences("BirdHotspots", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val existingHotspots = sharedPreferences.getString("userHotspots", "") ?: ""
        val newHotspotString = "${hotspot.name},${hotspot.latitude},${hotspot.longitude},${hotspot.description};"
        editor.putString("userHotspots", existingHotspots + newHotspotString)
        editor.apply()
    }
}
