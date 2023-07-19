package com.example.slide

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.slide.factory.SquareFactory
import com.example.slide.model.Slide

class SlideManager {
    private val slides = mutableListOf<Slide>()
    val slideCount: Int
        get() = slides.size

    fun getSlideData(index: Int): Slide = slides[index]

    @RequiresApi(Build.VERSION_CODES.O)
    fun addSlide() {
        slides.add(SquareFactory.createSquareSlide(213))
    }

    fun changeColor(index: Int, color: Int) {
        slides.getOrNull(index)?.backgroundColor = color
    }

    fun changeAlpha(index: Int, alpha: Int) {
        slides.getOrNull(index)?.alpha = alpha
    }

    fun getSlides() = slides
}