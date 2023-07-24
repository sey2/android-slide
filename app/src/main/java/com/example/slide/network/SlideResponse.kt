package com.example.slide.network
data class SlideResponse(
    val title: String,
    val creator: String,
    val slides: List<SlideData>
)
