package com.example.slide.factory

import android.graphics.Color
import com.example.slide.model.ImageSlide
import com.example.slide.model.Slide
import com.example.slide.model.SquareSlide
import java.util.UUID
import kotlin.random.Random

object SlideCreationFactory {
    private val usedIds = mutableSetOf<String>()

    fun createSquareSlide(sideLength: Int): Slide {
        val randomUUID = UUID.randomUUID().toString().split("-")
        var id: String

        do {
            id = buildString {
                randomUUID.forEachIndexed { index, uuid ->
                    if (index != 0) append("-")
                    append(uuid.substring(0, 3))
                }
            }
        } while (usedIds.contains(id))

        val color = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        val transparency = Random.nextInt(10) + 1

        return SquareSlide(id, sideLength, color, transparency)
    }

    fun createImageSlide(sideLength: Int): ImageSlide {
        val randomUUID = UUID.randomUUID().toString().split("-")
        var id: String

        do {
            id = buildString {
                randomUUID.forEachIndexed { index, uuid ->
                    if (index != 0) append("-")
                    append(uuid.substring(0, 3))
                }
            }
        } while (usedIds.contains(id))

        val transparency = Random.nextInt(10) + 1

        return ImageSlide(id, sideLength, transparency, null)
    }
}
