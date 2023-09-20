package com.example.wingspan

import retrofit2.Call
import com.example.wingspan.Models.Observation
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface EBirdApiService {

    @Headers(
        "X-eBirdApiToken:sc304p1n3apg"
    )
    @GET("data/obs/geo/recent")

    fun getBirdData(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("dist") distance: Int,
        @Query("maxResults") max: Int,
       // @Query("key") apiKey: String

    ): Call<Observation>

}