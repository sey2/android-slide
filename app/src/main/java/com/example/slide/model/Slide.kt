package com.example.slide.model

import com.example.slide.db.SlideEntity


interface Slide {
    val id: String
    var sideLength: Int
    var alpha: Int
    var lastPosition: Pair<Float, Float>
    fun toEntity(): SlideEntity
}
