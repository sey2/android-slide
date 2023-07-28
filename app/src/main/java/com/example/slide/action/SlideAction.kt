package com.example.slide.action

import com.example.slide.model.Slide

sealed class SlideAction {
    data class SelectSlide(val id: String) : SlideAction()
    data class ChangeImage(val slide: Slide, val newImageBytes: ByteArray) : SlideAction()
    data class SetSlides(val slides: List<Slide>) : SlideAction()
    object ChangeSelectedIndex: SlideAction()
    object ClearSelectedSlide : SlideAction()
    object ClearAllSlide : SlideAction()
}
