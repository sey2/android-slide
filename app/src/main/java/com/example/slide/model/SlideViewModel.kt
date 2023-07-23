package com.example.slide.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.slide.action.SlideAction
import com.example.slide.manager.SlideManager

class SlideViewModel(private val slideManager: SlideManager) : ViewModel() {
    private val _slides = MutableLiveData<List<Slide>>()
    val slides: LiveData<List<Slide>> = _slides

    private val _selectedSlide = MutableLiveData<Slide?>()
    val selectedSlide: LiveData<Slide?> = _selectedSlide

    private val _selectedSlideId = MutableLiveData<String?>()
    val selectedSlideId: LiveData<String?> = _selectedSlideId

    private val _selectedSlideIndex = MutableLiveData<Int?>()
    val selectedSlideIndex: LiveData<Int?> = _selectedSlideIndex

    fun processAction(action: SlideAction) {
        when (action) {
            is SlideAction.SelectSlide -> selectSlide(action.id)
            is SlideAction.ChangeImage -> changeImgByteArr(action.slide, action.newImageBytes)
            is SlideAction.AddSlide -> addSlide()
            is SlideAction.ChangeColor -> changeColor(action.color)
            is SlideAction.ChangeAlpha -> changeAlpha(action.alpha)
            is SlideAction.SetSlides -> setSlides(action.slides)
            is SlideAction.ClearSelectedSlide -> clearSelectedSlide()
            is SlideAction.ChangeSelectedIndex -> changeSelectedIndex()
        }
    }

    fun selectSlide(id: String) {
        _selectedSlide.value = slideManager.getSlideData(id)
        _selectedSlideId.value = id
    }

    fun selectSlide(index: Int) {
        _selectedSlide.value = slideManager.getSlideData(index)
    }

    private fun changeSelectedIndex() {
        _selectedSlideIndex.value = selectedSlide.value?.let { slideManager.getSlideIndex(it) }
    }

    fun changeImgByteArr(slide: Slide, newImageBytes: ByteArray) {
        slideManager.changImgByteArr(slide, newImageBytes)
        if (slide == _selectedSlide.value) {
            selectSlide(slide.id)
        }
        _slides.value = slideManager.getSlides()
    }

    private fun addSlide() {
        slideManager.addSlide()
        _slides.value = slideManager.getSlides()
    }

    private fun changeColor(color: Int) {
        val selectedSlide = _selectedSlide.value ?: return

        slideManager.changeColor(selectedSlide, color)
        _selectedSlide.value = slideManager.getSlideData(selectedSlide)
        _slides.value = slideManager.getSlides()
    }

    private fun changeAlpha(alpha: Int) {
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

    fun clearSelectedSlide() {
        _selectedSlideId.value = null
        _selectedSlide.value = null
    }
}

