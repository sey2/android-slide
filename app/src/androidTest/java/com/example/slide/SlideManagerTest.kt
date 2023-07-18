package com.example.slide

import com.example.slide.factory.SquareFactory
import org.junit.Assert
import org.junit.Test
import org.junit.Assert.*

class SlideManagerTest {

    @Test
    fun addSlide_withSlide_success() {
        val slideManager = SlideManager()
        val slide = SquareFactory.createSquareSlide(212)
        slideManager.addSlide(slide)

        assertEquals(1, slideManager.slideCount)
        assertEquals(slide, slideManager.getSlideData(0))
    }

    @Test
    fun createSquareSlide_withUniqueIds_success() {
        val generatedIds = mutableSetOf<String>()
        for (i in 1..10000) {
            val square = SquareFactory.createSquareSlide(i)
            val id = square.id
            Assert.assertFalse(generatedIds.contains(id))
            generatedIds.add(id)
        }
    }

    @Test
    fun changeColor_withSlideAndColor_success() {
        val slideManager = SlideManager()
        val slide = SquareFactory.createSquareSlide(212)
        slideManager.addSlide(slide)

        val newColor = 123456
        slideManager.changeColor(0, newColor)
        assertEquals(newColor, slideManager.getSlideData(0).backgroundColor)
    }

    @Test
    fun changeAlpha_withSlideAndAlpha_success() {
        val slideManager = SlideManager()
        val slide = SquareFactory.createSquareSlide(212)
        slideManager.addSlide(slide)

        val newAlpha = 5
        slideManager.changeAlpha(0, newAlpha)
        assertEquals(newAlpha, slideManager.getSlideData(0).alpha)
    }
}