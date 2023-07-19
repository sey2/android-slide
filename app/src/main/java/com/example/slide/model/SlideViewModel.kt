package com.example.slide.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.slide.SlideManager

class SlideViewModel : ViewModel() {
    var slideManager = SlideManager()

    private val _slides = MutableLiveData<List<Slide>>()
    val slides: LiveData<List<Slide>> = _slides

    private val _selectedSlide = MutableLiveData<Slide>()
    val selectedSlide: LiveData<Slide> = _selectedSlide

    fun selectSlide(index: Int) {
        _selectedSlide.value = slideManager.getSlideData(index)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addSlide() {
        slideManager.addSlide()
        _slides.value = slideManager.getSlides()
    }

    fun changeColor(index: Int, color: Int) {
        slideManager.changeColor(index, color)
        _selectedSlide.value = slideManager.getSlideData(index)
        _slides.value = slideManager.getSlides()
    }

    fun changeAlpha(index: Int, alpha: Int) {
        if (alpha in 1..10) {
            slideManager.changeAlpha(index, alpha)
            _selectedSlide.value = slideManager.getSlideData(index)
            _slides.value = slideManager.getSlides()
        }
    }
}