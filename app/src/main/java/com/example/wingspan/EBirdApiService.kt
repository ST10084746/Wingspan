package com.example.wingspan

import com.example.wingspan.Models.Hotspot
import retrofit2.Call
import retrofit2.http.GET
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
        @Query("fmt") fmt: String,

    ): Call<ArrayList<Hotspot>>

}