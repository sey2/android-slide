package com.example.slide.util

import android.graphics.Color
import kotlin.random.Random

object SlideColorUtil {
    fun generateRandomColor(): Int {
        val red = Random.nextInt(256) // 0부터 255 사이의 랜덤한 값
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)
        return Color.rgb(red, green, blue)
    }
}
