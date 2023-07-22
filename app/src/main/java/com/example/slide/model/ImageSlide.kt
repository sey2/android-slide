package com.example.slide.model

import android.os.Build
import androidx.annotation.RequiresApi

class ImageSlide(
    override val id: String,
    override var sideLength: Int,
    override var alpha: Int,
    var imageBytes: ByteArray? = byteArrayOf()
) : Slide {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun toString(): String =
        "($id), Slide:$sideLength, Alpha: $alpha"
}
