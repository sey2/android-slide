package com.example.slide.model

import DrawingSlide
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.slide.action.SlideAction
import com.example.slide.manager.SlideManager
import com.example.slide.util.SlideColorUtil
import kotlinx.coroutines.launch

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
            is SlideAction.SetSlides -> setSlides(action.slides)
            is SlideAction.ClearSelectedSlide -> clearSelectedSlide()
            is SlideAction.ChangeSelectedIndex -> changeSelectedIndex()
            is SlideAction.ClearAllSlide -> clearAllSlide()
        }
    }

    fun saveSlidesState() {
        slideManager.saveSlidesState()
    }

    fun loadSlidesState() {
        viewModelScope.launch {
            slideManager.loadSlidesState()
            _slides.value = slideManager.getSlides()
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

    fun onAddButtonLongClicked(): Boolean {
        Log.d("Test", "Clicked")
        viewModelScope.launch {
            slideManager.addSlideFromServer()
            _slides.value = slideManager.getSlides()
        }
        return true
    }

    fun setSlides(list: List<Slide>) {
        slideManager.setSlides(list)
        _slides.value = slideManager.getSlides()
    }

    fun clearSelectedSlide() {
        _selectedSlideId.value = null
        _selectedSlide.value = null
    }

    private fun clearAllSlide() {
        slideManager.clearDatabase()
        _slides.value = slideManager.getSlides()
    }

    fun onBackgroundColorButtonClicked() {
        val color = SlideColorUtil.generateRandomColor()
        val selectedSlideId = selectedSlideId.value

        selectedSlideId?.let {
            when (selectedSlide.value) {
                is SquareSlide, is DrawingSlide -> {
                    changeColor(color)
                }
            }
        }
    }

    fun onAlphaPlusButtonClicked() {
        changeAlpha(selectedSlide.value!!.alpha + 1)
    }
    fun onAlphaMinusButtonClicked() {
        changeAlpha(selectedSlide.value!!.alpha - 1)
    }

    fun onAddButtonClicked() {
        slideManager.addSlide()
        _slides.value = slideManager.getSlides()
    }
}
