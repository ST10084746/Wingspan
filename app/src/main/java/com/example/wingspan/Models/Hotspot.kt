package com.example.wingspan.Models

data class Hotspot(
    val countryCode: String,
    val lat: Double,
    val latestObsDt: String,
    val lng: Double,
    val locId: String,
    val locName: String,
    val numSpeciesAllTime: Int,
    val subnational1Code: String
)