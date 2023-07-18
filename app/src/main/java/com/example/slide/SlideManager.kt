package com.example.slide

import com.example.slide.model.Slide

class SlideManager {
    private val slides = mutableListOf<Slide>()
    val slideCount: Int
        get() = slides.size

    fun getSlideData(index: Int): Slide = slides[index]

    fun addSlide(slide: Slide) {
        slides.add(slide)
    }

    fun changeColor(index: Int, color: Int) {
        slides.getOrNull(index)?.backgroundColor = color
    }

    fun changeAlpha(index: Int, alpha: Int) {
        slides.getOrNull(index)?.alpha = alpha
    }

    fun getSlides() = slides
}