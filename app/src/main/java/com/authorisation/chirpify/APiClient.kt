package com.authorisation.chirpify

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APiClient {
    private const val BASE_URL = "https://api.ebird.org/"
    private const val EBIRD_API_TOKEN = "nc1meu0cup06" // Replace with your actual eBird API token

    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-eBirdApiToken", EBIRD_API_TOKEN)
                .build()
            chain.proceed(request)
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: eBirdService = retrofit.create(eBirdService::class.java)
    val hotspotApi: HotspotApi = retrofit.create(HotspotApi::class.java)
}
