package com.example.slide.manager

import DrawingSlide
import SlideRepository
import com.example.slide.model.ImageSlide
import com.example.slide.model.Slide
import com.example.slide.model.SquareSlide
import com.example.slide.network.SlideRemoteSource

class SlideManager {
    private val slides = mutableListOf<Slide>()
    private val slideRepository = SlideRepository()
    private val slideRemoteSource = SlideRemoteSource()
    val slideCount: Int
        get() = slides.size

    fun getSlideData(id: String): Slide? = slides.find { it.id == id }

    fun getSlideData(index: Int): Slide = slides[index]

    fun getSlideData(slide: Slide): Slide = slides[slides.indexOf(slide)]

    fun saveSlidesState() {
        slideRepository.saveSlides(slides)
    }

    suspend fun loadSlidesState() {
        slides.clear()
        slides.addAll(slideRepository.loadSlides())
    }

    fun addSlide() {
        slides.add(slideRepository.createRandomSlide(213))
    }

    fun changeColor(slide: Slide, color: Int) {
        if (slide is SquareSlide) {
            (slides.getOrNull(findSlideIndex(slide)) as SquareSlide).backgroundColor = color
        } else if (slide is DrawingSlide) {
            (slides.getOrNull(findSlideIndex(slide)) as DrawingSlide).paint.color = color
            (slides.getOrNull(findSlideIndex(slide)) as DrawingSlide).backgroundColor = color
        }

    }

    fun changImgByteArr(slide: Slide, newImageBytes: ByteArray) {
        val targetSlide = slides.find { it.id == slide.id }
        if (targetSlide is ImageSlide) {
            targetSlide.imageBytes = newImageBytes
        }
    }

    fun changeAlpha(slide: Slide, alpha: Int) {
        if (slide !is DrawingSlide) {
            slides.getOrNull(findSlideIndex(slide))?.alpha = alpha
        }
    }

    fun clearDatabase() {
        slideRepository.clearSlides()
        slides.clear()
    }

    private fun findSlideIndex(slide: Slide) = slides.indexOf(slide)

    fun setSlides(list: List<Slide>) {
        slides.clear()
        slides.addAll(list)
    }

    fun getSlides() = slides

    fun getSlideIndex(slide: Slide): Int = slides.indexOf(slide)
    suspend fun addSlideFromServer() {
        slides.addAll(slideRemoteSource.downloadSlidesFromServer())
    }
}