package com.example.slide.factory

import DrawingSlide
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.Base64
import com.example.slide.db.SlideEntity
import com.example.slide.drawing.PathData
import com.example.slide.drawing.PathOperation
import com.example.slide.drawing.PathOperationAdapter
import com.example.slide.model.ImageSlide
import com.example.slide.model.Slide
import com.example.slide.model.SquareSlide
import com.google.gson.GsonBuilder
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
        val randomColor = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        val paint = Paint().apply {
            color = randomColor
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }

        return DrawingSlide(id, 0, sideLength, randomColor, path, paint)
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

    fun createSlideFromEntity(slideEntity: SlideEntity): Slide {
        return when (slideEntity.type) {
            "ImageSlide" -> createImageSlideFromEntity(slideEntity)
            "DrawingSlide" -> createDrawingSlideFromEntity(slideEntity)
            "SquareSlide" -> createSquareSlideFromEntity(slideEntity)
            else -> throw IllegalArgumentException("Unknown slide type: ${slideEntity.type}")
        }
    }

    private fun createImageSlideFromEntity(slideEntity: SlideEntity): ImageSlide {
        return ImageSlide(
            id = slideEntity.id,
            sideLength = slideEntity.sideLength,
            alpha = slideEntity.alpha,
            imageBytes = slideEntity.imageBytes?.let {
                Base64.decode(
                    slideEntity.imageBytes,
                    Base64.DEFAULT
                )
            },
            lastPosition = Pair(slideEntity.lastX, slideEntity.lastY)
        )
    }

    private fun createDrawingSlideFromEntity(slideEntity: SlideEntity): DrawingSlide {
        val gson = GsonBuilder()
            .registerTypeAdapter(PathOperation::class.java, PathOperationAdapter())
            .create()
        val pathData = gson.fromJson(slideEntity.pathData, PathData::class.java)
        val path = Path()
        for (operation in pathData.operations) {
            when (operation) {
                is PathOperation.MoveTo -> path.moveTo(operation.x, operation.y)
                is PathOperation.LineTo -> path.lineTo(operation.x, operation.y)
                is PathOperation.QuadTo -> path.quadTo(operation.lastX, operation.lastY, operation.endX, operation.endY)
            }
        }
        return DrawingSlide(
            id = slideEntity.id,
            sideLength = slideEntity.sideLength,
            backgroundColor = slideEntity.backgroundColor,
            alpha = slideEntity.alpha,
            paint = Paint(),
            isEditable = false,
            path = path,
            minX = slideEntity.minX,
            minY = slideEntity.minY,
            maxX = slideEntity.maxX,
            maxY = slideEntity.maxY,
            pathData = pathData,
            lastPosition = Pair(slideEntity.lastX, slideEntity.lastY)
        )
    }

    private fun createSquareSlideFromEntity(slideEntity: SlideEntity): SquareSlide {
        return SquareSlide(
            id = slideEntity.id,
            sideLength = slideEntity.sideLength,
            alpha = slideEntity.alpha,
            backgroundColor = slideEntity.backgroundColor ?: 0,
            lastPosition = Pair(slideEntity.lastX, slideEntity.lastY)
        )
    }

}
