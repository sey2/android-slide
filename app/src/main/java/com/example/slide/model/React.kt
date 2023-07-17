package com.example.slide.model


data class React(
    override val id: String,
    val sideLength: Int,
    override val backgroundColor: Triple<Int, Int, Int>,
    override val alpha: Int
) : Slide {
    override fun toString(): String = "($id), Slide:$sideLength, R:${backgroundColor.first}, " +
                                        "G:${backgroundColor.second}, B:${backgroundColor.third}, Alpha: $alpha"
}

