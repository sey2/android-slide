package com.example.slide.network

import com.google.gson.annotations.SerializedName

data class ColorData(
    @SerializedName("R") val r: Int,
    @SerializedName("G") val g: Int,
    @SerializedName("B") val b: Int
)