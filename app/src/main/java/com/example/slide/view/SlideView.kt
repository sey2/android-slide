package com.example.slide.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.slide.R
import com.example.slide.model.SlideViewModel
import com.example.slide.util.ColorUtil

class SlideView(
    context: Context,
    attrs: AttributeSet?,
    private val svId: String,
    viewModel: SlideViewModel
) : View(context, attrs) {
    private val selectedStrokeWidth: Float = resources.getDimension(R.dimen.selected_stroke_width)
    private val selectedStrokeColor: Int = ContextCompat.getColor(context, R.color.red)
    private val defaultStrokeWidth: Float = resources.getDimension(R.dimen.default_stroke_width)
    private val defaultStrokeColor: Int = ContextCompat.getColor(context, R.color.transparent)
    private val strokePaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = defaultStrokeWidth
        color = defaultStrokeColor
    }

    private var dX = 0f
    private var dY = 0f

    init {
        isClickable = true
        isFocusable = true
        tag = svId

        viewModel.selectedSlideId.observe((context as LifecycleOwner), Observer { selectedSlideId ->
            isSelected = svId == selectedSlideId
            invalidate()
        })

        setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - motionEvent.rawX
                    dY = view.y - motionEvent.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    view.animate()
                        .x(motionEvent.rawX + dX)
                        .y(motionEvent.rawY + dY)
                        .setDuration(0)
                        .start()
                }

                MotionEvent.ACTION_UP -> {
                    isSelected = !isSelected
                    viewModel.selectSlide(svId)
                }
            }
            true
        }

        setBackgroundResource(R.color.yellow)

        val sizeInPixels = dpToPx(150)
        val layoutParams = ConstraintLayout.LayoutParams(sizeInPixels, sizeInPixels)

        layoutParams.startToStart = R.id.board_view
        layoutParams.endToEnd = R.id.board_view
        layoutParams.topToTop = R.id.board_view
        layoutParams.bottomToBottom = R.id.board_view

        this.layoutParams = layoutParams
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

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

}