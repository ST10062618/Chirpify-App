package com.authorisation.chirpify

// HotspotService.kt
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//ebird API
interface eBirdService {
    @GET("v2/ref/hotspots")
    suspend fun getHotspots(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double
    ): List<Hotspot>
}

