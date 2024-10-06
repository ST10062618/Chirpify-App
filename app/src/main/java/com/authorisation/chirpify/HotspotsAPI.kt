package com.authorisation.chirpify

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HotspotApi {
    @POST("hotspots")
    fun addHotspot(@Body hotspot: Hotspot): Call<HotspotResponse>

    @GET("ref/hotspot")
    fun getHotspots(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double
    ): Call<List<Hotspot>>
}
