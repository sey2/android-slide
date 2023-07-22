package com.example.slide.manager

import com.example.slide.factory.SlideCreationFactory
import com.example.slide.model.ImageSlide
import com.example.slide.model.Slide
import com.example.slide.model.SquareSlide
import kotlin.random.Random

class SlideManager {
    private val slides = mutableListOf<Slide>()

    val slideCount: Int
        get() = slides.size

    fun getSlideData(id: String): Slide? = slides.find { it.id == id }

    fun getSlideData(index: Int): Slide = slides[index]

    fun getSlideData(slide: Slide): Slide = slides[slides.indexOf(slide)]

    fun addSlide() {
        if (Random.nextBoolean()) {
            slides.add(SlideCreationFactory.createSquareSlide(213))
        } else {
            slides.add(SlideCreationFactory.createImageSlide(213))
        }
    }

    fun changeColor(slide: Slide, color: Int) {
        (slides.getOrNull(findSlideIndex(slide)) as SquareSlide)?.backgroundColor = color
    }

    fun changImgByteArr(slide: Slide, newImageBytes: ByteArray) {
        val targetSlide = slides.find { it.id == slide.id }
        if (targetSlide is ImageSlide) {
            targetSlide.imageBytes = newImageBytes
        }
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

    fun getSlideIndex(slide: Slide): Int = slides.indexOf(slide)
}