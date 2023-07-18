package com.example.slide.model

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

data class Square(
    override val id: String,
    val sideLength: Int,
    override var backgroundColor: Int,
    override var alpha: Int
) : Slide {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun toString(): String =
        "($id), Slide:$sideLength, R:${Color.red(backgroundColor)}, " +
                "G:${Color.green(backgroundColor)}, B:${Color.blue(backgroundColor)}}, Alpha: $alpha"
}

