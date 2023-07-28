package com.example.slide.model

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.slide.db.SlideEntity

data class SquareSlide(
    override val id: String,
    override var sideLength: Int,
    var backgroundColor: Int,
    override var alpha: Int,
    override var lastPosition: Pair<Float, Float> = Pair(0f, 0f)
) : Slide {

    override fun toString(): String =
        "($id), Slide:$sideLength, R:${Color.red(backgroundColor)}, " +
                "G:${Color.green(backgroundColor)}, B:${Color.blue(backgroundColor)}}, Alpha: $alpha"

    override fun toEntity(): SlideEntity {
        return SlideEntity(
            id = id,
            sideLength = sideLength,
            alpha = alpha,
            type = "SquareSlide",
            imageBytes = null,
            pathData = null,
            backgroundColor = backgroundColor,
            lastX = lastPosition.first,
            lastY = lastPosition.second
        )
    }
}

