package com.example.slide.network

import com.example.slide.network.SlideData

data class SlideResponse(
    val title: String,
    val creator: String,
    val slides: List<SlideData>
)
