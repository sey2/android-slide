package com.example.slide

import com.example.slide.factory.SlideCreationFactory
import com.example.slide.manager.SlideManager
import com.example.slide.model.SquareSlide
import org.junit.Assert
import org.junit.Test
import org.junit.Assert.*

class SlideManagerTest {

    @Test
    fun addSlide_withSlide_success() {
        val slideManager = SlideManager()
        slideManager.addSlide()

        assertEquals(1, slideManager.slideCount)
    }

    @Test
    fun createSquareSlide_withUniqueIds_success() {
        val generatedIds = mutableSetOf<String>()
        for (i in 1..10000) {
            val square = SlideCreationFactory.createSquareSlide(i)
            val id = square.id
            Assert.assertFalse(generatedIds.contains(id))
            generatedIds.add(id)
        }
    }

    @Test
    fun changeColor_withSlideAndColor_success() {
        val slideManager = SlideManager()
        slideManager.addSlide()

        val newColor = 123456
        val slide = slideManager.getSlideData(0)
        slideManager.changeColor(slide, newColor)
        assertEquals(newColor, (slide as SquareSlide).backgroundColor)
    }

    @Test
    fun changeAlpha_withSlideAndAlpha_success() {
        val slideManager = SlideManager()
        slideManager.addSlide()

        val newAlpha = 5
        slideManager.changeAlpha(slideManager.getSlideData(0), newAlpha)
        assertEquals(newAlpha, slideManager.getSlideData(0).alpha)
    }
}