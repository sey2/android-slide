package com.example.slide.model

import android.util.Base64
import com.example.slide.db.SlideEntity

class ImageSlide(
    override val id: String,
    override var sideLength: Int,
    override var alpha: Int,
    var imageBytes: ByteArray? = byteArrayOf(),
    override var lastPosition: Pair<Float, Float> = Pair(0f, 0f)
) : Slide {
    override fun toString(): String =
        "($id), Slide:$sideLength, Alpha: $alpha"

    override fun toEntity(): SlideEntity {
        return SlideEntity(
            id = id,
            sideLength = sideLength,
            alpha = alpha,
            type = "ImageSlide",
            imageBytes = imageBytes?.let { Base64.encodeToString(it, Base64.DEFAULT) },
            pathData = null,
            backgroundColor = null,
            lastX = lastPosition.first,
            lastY = lastPosition.second
        )
    }
}
