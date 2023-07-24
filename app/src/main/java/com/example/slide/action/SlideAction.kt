package com.example.slide.action

import com.example.slide.model.Slide

sealed class SlideAction {
    data class SelectSlide(val id: String) : SlideAction()
    data class ChangeImage(val slide: Slide, val newImageBytes: ByteArray) : SlideAction()
    data class ChangeColor(val color: Int) : SlideAction()
    data class ChangeAlpha(val alpha: Int) : SlideAction()
    data class SetSlides(val slides: List<Slide>) : SlideAction()
    object ChangeSelectedIndex: SlideAction()
    object AddSlide : SlideAction()
    object ClearSelectedSlide : SlideAction()
    object AddSlideFromServer : SlideAction()
}
