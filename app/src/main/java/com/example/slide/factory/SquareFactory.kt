package com.example.slide.factory

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.slide.model.Slide
import com.example.slide.model.Square
import java.util.UUID
import kotlin.random.Random

object SquareFactory {
    private val usedIds = mutableSetOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
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

        val color =
            Color.valueOf(Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)))
        val transparency = Random.nextInt(10) + 1

        return Square(id, sideLength, color.toArgb(), transparency)
    }
}
