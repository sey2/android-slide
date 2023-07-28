package com.example.slide.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SlideEntity(
    @PrimaryKey val id: String,
    val sideLength: Int,
    val alpha: Int,
    val type: String,
    val imageBytes: String?,
    val pathData: String?,
    val backgroundColor: Int?,
    var minX: Float = Float.MAX_VALUE,
    var minY: Float = Float.MAX_VALUE,
    var maxX: Float = Float.MIN_VALUE,
    var maxY: Float = Float.MIN_VALUE,
    var lastX: Float = 0f,
    var lastY: Float = 0f
)