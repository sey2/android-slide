package com.example.slide.adapter

import DrawingSlide
import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Button
import androidx.databinding.BindingAdapter
import com.example.slide.R
import com.example.slide.model.Slide
import com.example.slide.model.SquareSlide

object MenuBindingAdapter {

    @BindingAdapter("app:backgroundTint")
    @JvmStatic
    fun setBackgroundTint(button: Button, slide: Slide?) {
        val color = when (slide) {
            is DrawingSlide -> (slide as DrawingSlide).backgroundColor
            is SquareSlide -> (slide as SquareSlide).backgroundColor
            else -> R.color.white
        }

        button.backgroundTintList = ColorStateList.valueOf(color ?: Color.WHITE)
    }

    @BindingAdapter("app:btnBackText")
    @JvmStatic
    fun setBtnBackText(button: Button, slide: Slide?) {
        val hex = when (slide) {
            is DrawingSlide -> slide.backgroundColor
            is SquareSlide -> slide.backgroundColor
            else -> null
        }?.let { Integer.toHexString(it) } ?: ""

        button.text = if(hex.isBlank()) "" else "0x${hex.uppercase()}"
    }

}