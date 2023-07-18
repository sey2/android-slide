package com.example.slide

import androidx.lifecycle.MutableLiveData
import com.example.slide.model.Slide

class SlideManager {
    private val slides = mutableListOf<Slide>()
    private val slidesLiveData = MutableLiveData<List<Slide>>(slides)
    val slideCount: Int
        get() = slides.size

    fun getSlideData(index: Int): Slide = slides[index]

    fun addSlide(slide: Slide) {
        slides.add(slide)
        slidesLiveData.value = slides
    }

    fun changeColor(index: Int, color: Int) {
        slides.getOrNull(index)?.backgroundColor = color
        slidesLiveData.value = slides
    }

    fun changeAlpha(index: Int, alpha: Int) {
        slides.getOrNull(index)?.alpha = alpha
        slidesLiveData.value = slides
    }

    fun getSlides() = slides
}
