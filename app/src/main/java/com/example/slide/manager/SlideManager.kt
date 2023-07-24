package com.example.slide.manager

import android.graphics.Color
import com.example.slide.factory.SlideCreationFactory
import com.example.slide.model.ImageSlide
import com.example.slide.model.Slide
import com.example.slide.model.SquareSlide
import com.example.slide.network.SlideService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import kotlin.random.Random

class SlideManager {
    private val slides = mutableListOf<Slide>()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://public.codesquad.kr/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val slideService: SlideService = retrofit.create(SlideService::class.java)


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

    suspend fun addSlideFromServer() {
        val serverPath = if (Random.nextBoolean())
            "jk/softeer-bootcamp/square-only-slides.json"
        else
            "jk/softeer-bootcamp/image-slides.json"

        val response = slideService.getSlideData(serverPath)

        response.body()?.slides?.forEach { slideData ->
            when (slideData.type) {
                "Image" -> {
                    val imageBytes = downloadImage(slideData.url)
                    val newSlide = ImageSlide(slideData.id, 0, slideData.alpha, imageBytes)
                    slides.add(newSlide)
                }
                "Square" -> {
                    val color = slideData.color.let { Color.rgb(it.R, it.G, it.B) }
                    val newSlide = SquareSlide(slideData.id, slideData.size, color, slideData.alpha)
                    slides.add(newSlide)
                }
            }
        }
    }

    private suspend fun downloadImage(imageUrl: String): ByteArray = withContext(Dispatchers.IO) {
        URL(imageUrl).openStream().readBytes()
    }
}