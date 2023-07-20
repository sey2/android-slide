package com.example.slide

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.slide.factory.SquareFactory
import com.example.slide.model.Slide

class SlideManager {
    private val slides = mutableListOf<Slide>()

    val slideCount: Int
        get() = slides.size

    fun getSlideData(id: String): Slide? = slides.find { it.id == id }

    fun getSlideData(index: Int): Slide = slides[index]

    fun getSlideData(slide: Slide): Slide = slides[slides.indexOf(slide)]

    fun addSlide() {
        slides.add(SquareFactory.createSquareSlide(213))
    }

    fun changeColor(slide: Slide, color: Int) {
        slides.getOrNull(findSlideIndex(slide))?.backgroundColor = color
    }

    fun changeAlpha(slide: Slide, alpha: Int) {
        slides.getOrNull(findSlideIndex(slide))?.alpha = alpha
    }

    private fun findSlideIndex(slide: Slide) = slides.indexOf(slide)

    fun setSlides(list: List<Slide>) {
        slides.clear()
        slides.addAll(list)
    }

    fun getSlides() = slides
}