package com.example.slide.network

data class SlideData(
    val type: String,
    val id: String,
    val url: String,
    val alpha: Int,
    val size: Int,
    val color: ColorData
)
