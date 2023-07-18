package com.example.slide.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.slide.R
import com.example.slide.util.ColorUtil

class SlideView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val selectedStrokeWidth: Float = resources.getDimension(R.dimen.selected_stroke_width)
    private val selectedStrokeColor: Int = ContextCompat.getColor(context, R.color.blue)
    private val defaultStrokeWidth: Float = resources.getDimension(R.dimen.default_stroke_width)
    private val defaultStrokeColor: Int = ContextCompat.getColor(context, R.color.transparent)
    private val strokePaint: Paint = Paint()

    init {
        isClickable = true
        isFocusable = true

        setOnClickListener {
            isSelected = !isSelected
            invalidate()
        }

        setBackgroundResource(R.color.yellow)

        strokePaint.isAntiAlias = true
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = defaultStrokeWidth
        strokePaint.color = defaultStrokeColor
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isSelected) {
            strokePaint.strokeWidth = selectedStrokeWidth
            strokePaint.color = selectedStrokeColor
        } else {
            strokePaint.strokeWidth = defaultStrokeWidth
            strokePaint.color = defaultStrokeColor
        }
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), strokePaint)
    }

    fun changeColor(backgroundColor: Int, alpha: Int) {
        setBackgroundColor(ColorUtil.convertIntToArgb(backgroundColor, alpha))
    }


}
