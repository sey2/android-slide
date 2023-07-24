package com.example.slide.factory

import DrawingSlide
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.example.slide.model.ImageSlide
import com.example.slide.model.Slide
import com.example.slide.model.SquareSlide
import java.util.UUID
import kotlin.random.Random

object SlideCreationFactory {
    private val usedIds = mutableSetOf<String>()

    fun createSquareSlide(sideLength: Int): Slide {
        val id = createUUID()

        val color = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        val transparency = Random.nextInt(10) + 1

        return SquareSlide(id, sideLength, color, transparency)
    }

    fun createImageSlide(sideLength: Int): ImageSlide {
        val id = createUUID()
        val transparency = Random.nextInt(10) + 1

        return ImageSlide(id, sideLength, transparency, null)
    }

    fun createDrawingSlide(sideLength: Int): DrawingSlide {
        var id = createUUID()

        val path = Path()
        val paint = Paint().apply {
            color = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }

        return DrawingSlide(id, 0, sideLength, path, paint)
    }


    private fun createUUID(): String {
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

        return id
    }
}
