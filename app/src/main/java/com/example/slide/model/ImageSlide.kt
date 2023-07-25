package com.example.slide.model
class ImageSlide(
    override val id: String,
    override var sideLength: Int,
    override var alpha: Int,
    var imageBytes: ByteArray? = byteArrayOf()
) : Slide {
    override fun toString(): String =
        "($id), Slide:$sideLength, Alpha: $alpha"
}
