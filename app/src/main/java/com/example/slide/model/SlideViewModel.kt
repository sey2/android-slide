package com.example.slide.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.slide.SlideManager

class SlideViewModel(private val slideManager: SlideManager) : ViewModel(){
    private val _slides = MutableLiveData<List<Slide>>()
    val slides: LiveData<List<Slide>> = _slides

    private val _selectedSlide = MutableLiveData<Slide?>()
    val selectedSlide: LiveData<Slide?> = _selectedSlide

    private val _selectedSlideId = MutableLiveData<String?>()
    val selectedSlideId: LiveData<String?> = _selectedSlideId

    fun selectSlide(id: String){
        _selectedSlide.value = slideManager.getSlideData(id)
        _selectedSlideId.value = id
    }

    fun selectSlide(index: Int) {
        _selectedSlide.value = slideManager.getSlideData(index)
    }

    fun addSlide() {
        slideManager.addSlide()
        _slides.value = slideManager.getSlides()
    }

    fun changeColor(color: Int) {
        val selectedSlide = _selectedSlide.value ?: return

        slideManager.changeColor(selectedSlide, color)
        _selectedSlide.value = slideManager.getSlideData(selectedSlide)
        _slides.value = slideManager.getSlides()
    }

    fun changeAlpha(alpha: Int) {
        val selectedSlide = _selectedSlide.value ?: return

        if (alpha !in 1..10) return

        slideManager.changeAlpha(selectedSlide, alpha)
        _selectedSlide.value = slideManager.getSlideData(selectedSlide)
        _slides.value = slideManager.getSlides()
    }

    fun setSlides(list: List<Slide>) {
        slideManager.setSlides(list)
        _slides.value = slideManager.getSlides()
    }
    fun clearSelectedSlide(){
        _selectedSlideId.value = null
        _selectedSlide.value = null
    }
}

