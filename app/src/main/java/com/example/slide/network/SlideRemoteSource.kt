package com.example.slide.network

import android.graphics.Color
import com.example.slide.BuildConfig
import com.example.slide.model.ImageSlide
import com.example.slide.model.Slide
import com.example.slide.model.SquareSlide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import kotlin.random.Random

class SlideRemoteSource {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val slideService: SlideService = retrofit.create(SlideService::class.java)

    suspend fun downloadSlidesFromServer(): List<Slide> {
        val serverPath = if (Random.nextBoolean())
            BuildConfig.SERVER_PATH1
        else
            BuildConfig.SERVER_PATH2

        val response = slideService.getSlideData(serverPath)

        return if (response.isSuccessful) {
            response.body()?.slides?.map { slideData ->
                when (slideData.type) {
                    "Image" -> {
                        val imageBytes = downloadImage(slideData.url)
                        ImageSlide(slideData.id, 0, slideData.alpha, imageBytes)
                    }
                    "Square" -> {
                        val color = slideData.color.let { Color.rgb(it.R, it.G, it.B) }
                        SquareSlide(slideData.id, slideData.size, color, slideData.alpha)
                    }
                    else -> throw IllegalArgumentException("Unknown slide type: ${slideData.type}")
                }
            } ?: throw IllegalStateException("Body is null")
        } else {
            throw HttpException(response)
        }

    }

    private suspend fun downloadImage(imageUrl: String): ByteArray = withContext(Dispatchers.IO) {
        URL(imageUrl).openStream().readBytes()
    }
}