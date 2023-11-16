package com.example.wingspan.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class BirdInfoItem(
    val bandingCodes: List<String>,
    val category: String,
    val comName: String,
    val comNameCodes: List<String>,
    val extinct: Boolean,
    val extinctYear: Int,
    val familyCode: String,
    val familyComName: String,
    val familySciName: String,
    val order: String,
    val reportAs: String,
    val sciName: String,
    val sciNameCodes: List<String>,
    val speciesCode: String,
    val taxonOrder: Double
):Parcelable